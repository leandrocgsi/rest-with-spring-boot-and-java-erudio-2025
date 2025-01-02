package br.com.erudio.file.exporter;

import org.springframework.core.io.Resource;

public record FileExportResponse(Resource resource, String fileName) {}
