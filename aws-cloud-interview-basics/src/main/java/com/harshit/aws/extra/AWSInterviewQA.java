package com.harshit.aws.extra;

/**
 * AWS cloud fundamentals interview Q&A — plain Java (no SDK required) under aws-cloud-interview-basics.
 *
 * <p><b>Q. What is AWS?</b> Amazon’s cloud platform offering compute, storage, networking, databases, ML, etc.,
 * billed pay-as-you-go. Real life: host a global SaaS without owning data centers.
 *
 * <p><b>Q. EC2?</b> Elastic Compute — virtual machines you size (CPU/RAM) and scale horizontally behind a load
 * balancer. Real life: Spring Boot on Ubuntu AMI with autoscaling group.
 *
 * <p><b>Q. S3?</b> Object storage (buckets, keys); durable, cheap for files, static sites, backups. Real life:
 * store invoice PDFs with lifecycle rules to Glacier.
 *
 * <p><b>Q. IAM?</b> Identity and Access Management — users, roles, policies (least privilege). Real life: Lambda
 * execution role can write only to a specific S3 prefix.
 *
 * <p><b>Q. VPC?</b> Virtual private cloud: subnets, route tables, NACLs, security groups; isolates network.
 * Real life: public subnets for ALB, private subnets for app + RDS.
 *
 * <p><b>Q. Load balancer in AWS?</b> ALB (HTTP L7), NLB (TCP/UDP L4), CLB (legacy). Real life: ALB routes
 * /api/* to ECS service.
 *
 * <p><b>Q. Auto Scaling?</b> Scale EC2/ECS tasks based on CPU, queue depth, schedules. Real life: add instances
 * when p95 latency rises.
 *
 * <p><b>Q. Lambda?</b> Function-as-a-service; event-driven, no servers to patch; pay per invoke/duration.
 * Real life: thumbnail generation on S3 upload events.
 *
 * <p>The {@code main} method prints a tiny “architecture sketch” as strings — interview answers are in Javadoc.
 */
public final class AWSInterviewQA {

    private AWSInterviewQA() {
    }

    public static void main(String[] args) {
        String sketch = ""
                + "Internet -> Route53\n"
                + "  -> ALB (public subnet)\n"
                + "  -> EC2/ECS tasks (private subnet) with IAM role\n"
                + "  -> RDS (private) + ElastiCache optional\n"
                + "  -> S3 for static assets / exports\n";
        System.out.println("Sample AWS layout (talking point):\n" + sketch);
    }
}
