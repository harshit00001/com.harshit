package com.harshit.transaction.proxy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * No interface — Spring uses CGLIB subclass proxy for @Transactional.
 */
@Service
public class LegacyBatchProcessor {

    @Transactional
    public void runBatch() {
        // no-op demo
    }
}
