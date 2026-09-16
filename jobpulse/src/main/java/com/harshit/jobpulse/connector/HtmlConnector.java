package com.harshit.jobpulse.connector;

import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.config.ConnectorType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Fallback for career pages that render job lists server-side. All selectors come from
 * configuration, so a new company needs a config block rather than a new class.
 *
 * <p>This cannot read pages that build their list in the browser with JavaScript — those
 * need either the vendor API (preferred) or a headless browser.
 */
@Component
public class HtmlConnector implements JobConnector {

    private static final Logger log = LoggerFactory.getLogger(HtmlConnector.class);

    private final HttpFetcher httpFetcher;

    public HtmlConnector(HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public ConnectorType type() {
        return ConnectorType.HTML;
    }

    @Override
    public List<RawJob> fetch(CompanyConfig company) {
        String listUrl = company.option("listUrl", company.getCareerUrl());
        if (listUrl == null || listUrl.isBlank()) {
            throw new ConnectorException("Company " + company.getId() + " has no listUrl/careerUrl");
        }

        String itemSelector = company.requiredOption("itemSelector");
        String titleSelector = company.option("titleSelector", null);
        String locationSelector = company.option("locationSelector", null);
        String linkSelector = company.option("linkSelector", "a");

        Document document = Jsoup.parse(httpFetcher.getHtml(listUrl), listUrl);
        Elements items = document.select(itemSelector);
        List<RawJob> jobs = new ArrayList<>();

        for (Element item : items) {
            if (jobs.size() >= company.getMaxJobs()) {
                break;
            }
            String title = selectText(item, titleSelector);
            if (title == null || title.isBlank()) {
                continue;
            }
            String href = item.select(linkSelector).stream()
                    .map(link -> link.absUrl("href"))
                    .filter(url -> !url.isBlank())
                    .findFirst()
                    .orElse(listUrl);

            RawJob job = new RawJob();
            job.setExternalId(href);
            job.setTitle(title);
            job.setLocationText(selectText(item, locationSelector));
            job.setCity(job.getLocationText());
            job.setUrl(href);
            job.setApplyUrl(href);
            job.setDescription(item.text());
            jobs.add(job);
        }

        if (jobs.isEmpty()) {
            log.warn("HTML {}: selector '{}' matched nothing — the page is likely "
                    + "JavaScript-rendered, use the vendor API instead",
                    company.getId(), itemSelector);
        } else {
            log.info("HTML {}: {} postings parsed", company.getId(), jobs.size());
        }
        return jobs;
    }

    private String selectText(Element item, String selector) {
        if (selector == null || selector.isBlank()) {
            return item.text();
        }
        Element found = item.selectFirst(selector);
        return found == null ? null : found.text();
    }
}
