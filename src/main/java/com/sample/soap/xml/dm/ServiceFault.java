package com.sample.soap.xml.dm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceFault", propOrder = { "messages" })
public class ServiceFault {

    protected MessagesType messages;

    public MessagesType getMessages() {
        return messages;
    }

    public void setMessages(MessagesType messages) {
        this.messages = messages;
    }

}
