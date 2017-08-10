package com.example.spring.boot.rest.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.jvnet.ws.wadl.Application;
import org.jvnet.ws.wadl.Doc;
import org.jvnet.ws.wadl.Param;
import org.jvnet.ws.wadl.ParamStyle;
import org.jvnet.ws.wadl.Representation;
import org.jvnet.ws.wadl.Request;
import org.jvnet.ws.wadl.Resource;
import org.jvnet.ws.wadl.Resources;
import org.jvnet.ws.wadl.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@RestController
public class MaintenanceController {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceController.class);

    String xs_namespace = "http://www.w3.org/2001/XMLSchema";
    String LANG = "EN";

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @SuppressWarnings("rawtypes")
    private Class[] restControllers = { SpringBootExternalRestServiceController.class };

    @SuppressWarnings("rawtypes")
    @RequestMapping(method = RequestMethod.GET, value = "/application.wadl", produces = { "application/xml" })
    public @ResponseBody Application generateWadl(HttpServletRequest request) {
        Application result = new Application();
        Doc doc = new Doc();
        doc.setTitle("WADL for InventoryServiceOld");
        result.getDoc().add(doc);
        Resources wadResources = new Resources();
        wadResources.setBase(getBaseUrl(request));

        Map<RequestMappingInfo, HandlerMethod> handletMethods = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handletMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            Object object = handlerMethod.getBean();
            Object bean = webApplicationContext.getBean(object.toString());

            boolean isRestContoller = bean.getClass().isAnnotationPresent(RestController.class);
            String beanName = bean.getClass().getName();
            for (Class cl : restControllers) {
                String strCl = cl.getName();
                if (!isRestContoller && beanName.contains(strCl)) {
                    isRestContoller = true;
                }
            }

            if (!isRestContoller) {
                continue;
            }
            RequestMappingInfo mappingInfo = entry.getKey();

            Set<String> pattern = mappingInfo.getPatternsCondition().getPatterns();
            Set<RequestMethod> httpMethods = mappingInfo.getMethodsCondition().getMethods();
            ProducesRequestCondition producesRequestCondition = mappingInfo.getProducesCondition();
            Set<MediaType> mediaTypes = producesRequestCondition.getProducibleMediaTypes();
            Resource wadlResource = null;
            for (RequestMethod httpMethod : httpMethods) {
                org.jvnet.ws.wadl.Method wadlMethod = new org.jvnet.ws.wadl.Method();

                for (String uri : pattern) {
                    wadlResource = createOrFind(uri, wadResources);
                    wadlResource.setPath(uri);
                }

                wadlMethod.setName(httpMethod.name());
                Method javaMethod = handlerMethod.getMethod();
                wadlMethod.setId(javaMethod.getName());
                Doc wadlDocMethod = new Doc();
                wadlDocMethod.setTitle(javaMethod.getDeclaringClass().getSimpleName() + "." + javaMethod.getName());

                Annotation[] methodAnnotations = javaMethod.getAnnotations();
                ApiOperation apiO = null;
                for (int j = 0; j < methodAnnotations.length; j++) {
                    Annotation a = methodAnnotations[j];
                    if (a instanceof ApiOperation) {
                        apiO = (ApiOperation) a;
                        if (StringUtils.isNotBlank(apiO.value())) {
                            wadlDocMethod.getContent().add(apiO.value());
                        }
                        if (StringUtils.isNotBlank(apiO.notes())) {
                            wadlDocMethod.getContent().add(apiO.notes());
                        }
                    }
                }
                wadlMethod.getDoc().add(wadlDocMethod);

                // Request
                Request wadlRequest = new Request();

                Annotation[][] annotations = javaMethod.getParameterAnnotations();

                Class<?>[] paramTypes = javaMethod.getParameterTypes();
                for (int i = 0; i < annotations.length; i++) {
                    Class<?> paramType = paramTypes[i];
                    Annotation[] annotation = annotations[i];

                    Doc paramDoc = null;
                    Param waldParam = null;
                    for (Annotation annotation2 : annotation) {
                        if (annotation2 instanceof RequestParam) {
                            RequestParam param2 = (RequestParam) annotation2;
                            QName nm = convertJavaToXMLType(paramType);
                            waldParam = new Param();
                            waldParam.setType(nm);
                            waldParam.setName(param2.value());
                            waldParam.setStyle(ParamStyle.QUERY);
                            waldParam.setRequired(param2.required());
                            String defaultValue = cleanDefault(param2.defaultValue());
                            if (!defaultValue.equals("")) {
                                waldParam.setDefault(defaultValue);
                            }
                        } else if (annotation2 instanceof PathVariable) {
                            PathVariable param2 = (PathVariable) annotation2;
                            QName nm = convertJavaToXMLType(paramType);
                            waldParam = new Param();
                            waldParam.setType(nm);
                            waldParam.setName(param2.value());
                            waldParam.setStyle(ParamStyle.TEMPLATE);
                            waldParam.setRequired(true);
                        } else if (annotation2 instanceof ApiParam) {
                            ApiParam param2 = (ApiParam) annotation2;
                            paramDoc = new Doc();
                            paramDoc.setTitle(param2.name());
                            paramDoc.getContent().add(param2.value());
                        }
                    }
                    if (waldParam != null) {
                        if (paramDoc != null) {
                            waldParam.getDoc().add(paramDoc);
                        }
                        wadlRequest.getParam().add(waldParam);
                    }
                }
                if (!wadlRequest.getParam().isEmpty()) {
                    wadlMethod.setRequest(wadlRequest);
                }

                // Response
                if (!mediaTypes.isEmpty()) {
                    Response wadlResponse = new Response();
                    @SuppressWarnings("unused")
                    Class<? extends MethodParameter> methodReturn = handlerMethod.getReturnType().getClass();
                    ResponseStatus status = handlerMethod.getMethodAnnotation(ResponseStatus.class);
                    if (status == null) {
                        wadlResponse.getStatus().add((long) (HttpStatus.OK.value()));
                    } else {
                        HttpStatus httpcode = status.value();
                        wadlResponse.getStatus().add((long) httpcode.value());
                    }

                    for (MediaType mediaType : mediaTypes) {
                        Representation wadlRepresentation = new Representation();
                        wadlRepresentation.setMediaType(mediaType.toString());

                        if (apiO != null) {
                            if (apiO.response() != null) {

                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Response " + apiO.response());
                                }

                                Doc respDoc = new Doc();
                                respDoc.setTitle("Response");
                                respDoc.getContent().add(apiO.response());
                                wadlRepresentation.getDoc().add(respDoc);

                            }
                        }

                        wadlResponse.getRepresentation().add(wadlRepresentation);
                    }
                    wadlMethod.getResponse().add(wadlResponse);
                }

                wadlResource.getMethodOrResource().add(wadlMethod);

            }
        }
        result.getResources().add(wadResources);
        return result;
    }

    private QName convertJavaToXMLType(Class<?> type) {
        QName nm = new QName("");
        String classname = type.toString();
        if (classname.indexOf("String") >= 0) {
            nm = new QName(xs_namespace, "string", "xs");

        } else if (classname.indexOf("Integer") >= 0) {
            nm = new QName(xs_namespace, "int", "xs");
        } else if (classname.indexOf("Boolean") >= 0 || classname.indexOf("boolean") >= 0) {
            nm = new QName(xs_namespace, "boolean", "xs");
        }
        return nm;
    }

    private Resource createOrFind(String uri, Resources wadResources) {
        List<Resource> current = wadResources.getResource();
        for (Resource resource : current) {
            if (resource.getPath().equalsIgnoreCase(uri)) {
                return resource;
            }
        }
        Resource wadlResource = new Resource();
        current.add(wadlResource);
        return wadlResource;
    }

    @SuppressWarnings("unused")
    private String getUrl(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + requestUri;
    }

    private String getBaseUrl(HttpServletRequest request) {
        // String requestUri = request.getRequestURI();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    private String cleanDefault(String value) {
        value = value.replaceAll("\t", "");
        value = value.replaceAll("\n", "");
        return value;
    }
}
