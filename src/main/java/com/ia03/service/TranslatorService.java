package com.ia03.service;

import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
public class TranslatorService {
  private static ResourceBundleMessageSource messageSource;

  public TranslatorService(ResourceBundleMessageSource messageSource) {
    TranslatorService.messageSource = messageSource;
  }

  public static String toLocale(String messageCode) {
    Locale locale = LocaleContextHolder.getLocale();

    return messageSource.getMessage(messageCode, null, messageCode, locale);
  }
}
