package com.harshit.resume.web;

import com.harshit.resume.model.AtsReport;
import com.harshit.resume.model.ResumeDocument;
import com.harshit.resume.model.TailorResponse;
import com.harshit.resume.service.AtsScoringService;
import com.harshit.resume.service.PdfExportService;
import com.harshit.resume.service.ResumeStorageService;
import com.harshit.resume.service.ResumeTailorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ResumeApiController {

    private final ResumeStorageService storage;
    private final AtsScoringService atsScoringService;
    private final ResumeTailorService tailorService;
    private final PdfExportService pdfExportService;

    public ResumeApiController(ResumeStorageService storage,
                               AtsScoringService atsScoringService,
                               ResumeTailorService tailorService,
                               PdfExportService pdfExportService) {
        this.storage = storage;
        this.atsScoringService = atsScoringService;
        this.tailorService = tailorService;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/resume")
    public ResumeDocument getResume() {
        return storage.get();
    }

    @PutMapping("/resume")
    public ResumeDocument updateResume(@RequestBody ResumeDocument resume) throws IOException {
        storage.save(resume);
        return storage.get();
    }

    @PostMapping("/resume/reset")
    public ResumeDocument resetResume() throws IOException {
        storage.resetToDefault();
        return storage.get();
    }

    @PostMapping("/ats/analyze")
    public AtsReport analyze(@RequestBody JdRequest request) {
        return atsScoringService.score(storage.get(), request.jobDescription());
    }

    @PostMapping("/ats/tailor")
    public TailorResponse tailor(@RequestBody JdRequest request) throws IOException {
        TailorResponse response = tailorService.tailor(storage.get(), request.jobDescription());
        storage.save(response.getTailoredResume());
        return response;
    }

    @GetMapping("/resume/pdf")
    public ResponseEntity<byte[]> downloadPdf() throws Exception {
        byte[] pdf = pdfExportService.export(storage.get());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume-tailored.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    public record JdRequest(String jobDescription) {
    }
}
