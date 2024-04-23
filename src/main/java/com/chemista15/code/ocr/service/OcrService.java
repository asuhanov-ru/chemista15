package com.chemista15.code.ocr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
    If Tesseract API instance fail to initialize see
    AppData\Local\Temp\lept4j\win32-x86-64>
    AppData\Local\Temp\tess4j\win32-x86-64>
    both must contain same couple of dlls
 */
@Service
@Transactional
public class OcrService {

    private final Logger log = LoggerFactory.getLogger(OcrService.class);
}
