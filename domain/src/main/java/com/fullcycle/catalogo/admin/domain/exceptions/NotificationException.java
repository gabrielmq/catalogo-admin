package com.fullcycle.catalogo.admin.domain.exceptions;

import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final String aMessage, final Notification aNotification) {
        super(aMessage, aNotification.getErrors());
    }
}
