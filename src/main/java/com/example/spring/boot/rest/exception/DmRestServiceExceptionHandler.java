package com.example.spring.boot.rest.exception;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.example.spring.boot.rest.validator.FrameworkValidationError;

/**
 * This is the main exception handler for the External Demand REST services. Add
 * the {@link org.springframework.web.bind.annotation.ControllerAdvice}
 * annotation to the subclass. This will cause your service to use it for error
 * handling.
 * 
 * @author mlahariya
 * @version 1.0, Jan 2017
 */

@SuppressWarnings("deprecation")
@EnableWebMvc
@ControllerAdvice
public class DmRestServiceExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DmRestServiceExceptionHandler.class);

    @Autowired
    @Qualifier("MessageSource")
    private ResourceBundleMessageSource messageSource;

    /**
     * Used for handling FrameworkValidationError and FrameworkError which
     * further wrap up the obtained errorResponse into ResponseEntity.
     * 
     * @param req
     *            A HttpServletRequest
     * @param ex
     *            A Throwable
     * @return ResponseEntity with errorResponse and status.
     */

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<Object> handleControllerException(HttpServletRequest req, Throwable ex) {

        LOG.warn("Exception occurred for req[" + req + "]   ex: ", ex);
        ErrorResponse er = null;
        HttpStatus status = null;

        if (ex instanceof FrameworkError) {
            FrameworkError re = (FrameworkError) ex;
            ErrorResponse errorResponse = new ErrorResponse();
            if (re.getErrorMessage() != null) {
                Message m = re.getErrorMessage();
                errorResponse.setCode(re.getErrorMessage().getCode());
                if (m.getText() != null) {
                    errorResponse.setMessage(m.getText());
                } else {
                    errorResponse.setMessage(messageSource.getMessage(m.getCode(), m.getArgs(), Locale.getDefault()));
                }
            }
            errorResponse.setDetails(re.getDetails());
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
            errorResponse.setType(ErrorResponse.class.getCanonicalName());
            er = errorResponse;
            status = errorResponse.getStatus();
        }

        else if (ex instanceof FrameworkValidationError) {

            LOG.info("Validation error");
            FrameworkValidationError re = (FrameworkValidationError) ex;
            ErrorResponse errorResponse = new ErrorResponse();

            if (re.getErrorMessage() != null) {
                Message m = re.getErrorMessage();
                errorResponse.setCode(re.getErrorMessage().getCode());
                if (m.getText() != null) {
                    errorResponse.setMessage(m.getText());
                } else {
                    errorResponse.setMessage(messageSource.getMessage(m.getCode(), m.getArgs(), Locale.getDefault()));
                }
            }
            errorResponse.setDetails(re.getDetails());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST);
            errorResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
            errorResponse.setType(ErrorResponse.class.getCanonicalName());
            er = errorResponse;
            status = errorResponse.getStatus();

        }

        else {
            ErrorResponse errorResponse = new ErrorResponse(ex);
            er = errorResponse;
            status = errorResponse.getStatus();
        }

        Object response = null;
        if (er != null) {
            response = er;
            LOG.warn("[" + req.getMethod() + "::" + req.getContextPath() + "]   ErrorResponse: HttpStatus["
                    + er.getStatus() + "]  code[" + er.getCode() + "]  message[" + er.getMessage() + "]   timestamp["
                    + er.getTimestamp() + "]  details[" + er.getDetails() + "]");

            LOG.info(req.getParameterNames() + "  xxxx  " + req.getParameterMap());
        }

        return new ResponseEntity<Object>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleNoSuchRequestHandlingMethod(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("path", request.getContextPath());
        responseBody.put("message", "The URL you have reached is not in service at this time (404).");
        return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        LOG.warn("Exception occurred for body[" + body + "]   ex: ", ex);

        status = (status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status);
        Object response = null;
        if (ex instanceof FrameworkError) {
            FrameworkError re = (FrameworkError) ex;
            ErrorResponse errorResponse = new ErrorResponse();
            if (re.getErrorMessage() != null) {
                Message m = re.getErrorMessage();
                errorResponse.setCode(re.getErrorMessage().getCode());
                if (m.getText() != null) {
                    errorResponse.setMessage(m.getText());
                } else {
                    errorResponse.setMessage(messageSource.getMessage(m.getCode(), m.getArgs(), Locale.getDefault()));
                }
            }
            errorResponse.setDetails(re.getDetails());
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            errorResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
            errorResponse.setType(ErrorResponse.class.getCanonicalName());
            response = errorResponse;
            status = errorResponse.getStatus();
        } else {
            ErrorResponse errorResponse = new ErrorResponse(ex);
            response = errorResponse;
            status = errorResponse.getStatus();
        }
        return new ResponseEntity<Object>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String msg = null;
        if (request instanceof ServletWebRequest) {
            HttpServletRequest hsr = (HttpServletRequest) ((ServletWebRequest) request).getNativeRequest();
            msg = "The '" + hsr.getRequestURI() + "' endpoint does not support the '" + hsr.getContentType()
                    + "' media type.  Please check the docs and try again.";
        } else {
            LOG.warn("Unknown request object type.  Please update code to handle this: {}", request.getClass()
                    .getName());
            msg = "The target endpoint does not support the requested content type.";
        }
        FrameworkError newBse = new FrameworkError(msg, ex);
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }
        return handleExceptionInternal(newBse, null, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleServletRequestBindingException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleConversionNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleHttpMessageNotWritable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMissingServletRequestPart(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return super.handleBindException(ex, headers, status, request);
    }

}
