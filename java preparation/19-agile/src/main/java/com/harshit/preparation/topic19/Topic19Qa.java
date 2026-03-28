package com.harshit.preparation.topic19;

import java.util.List;

/**
 * Topic 19 — Agile / Scrum (behavioral scripts).
 */
public final class Topic19Qa {

    private Topic19Qa() {
    }

    /*
     * Q: What did you do while leading a team of four?
     *
     * SCRIPT:
     * I facilitated sprint planning and backlog refinement so scope was clear before coding started.
     * I ran daily stand-ups focused on unblocking people, not micromanaging status. I championed code
     * reviews and a shared definition of done. I paired with juniors on design and debugging, and I
     * communicated risks early to product stakeholders. My goal was predictable delivery without burning out the team.
     */

    /*
     * Q: Sprint planning?
     *
     * SCRIPT:
     * The team pulls work from the prioritized backlog into the sprint based on capacity and the sprint goal.
     * We break stories into tasks, estimate effort, and clarify acceptance criteria so surprises are rare during the sprint.
     */

    /*
     * Q: Daily stand-up?
     *
     * SCRIPT:
     * Fifteen minutes max: what I did yesterday, what I will do today, what blocks me. It is a sync
     * for developers to coordinate, not a status report upward—if it becomes long, I take detailed
     * discussions offline.
     */

    /*
     * Q: Sprint review?
     *
     * SCRIPT:
     * We demo working software to stakeholders, gather feedback, and adjust the backlog. It validates
     * that we built the right thing, not only that we wrote code.
     */

    /*
     * Q: Sprint retrospective?
     *
     * SCRIPT:
     * The team reflects on process: what helped, what hurt, and what we will change next sprint with
     * concrete action items. Psychological safety matters—people must feel safe naming problems or retros fail.
     */

    /** Tiny “sprint velocity” math — optional hook when you want runnable output with the behavioral scripts. */
    public static void demo() {
        List<Integer> storyPointsLastThreeSprints = List.of(21, 18, 24);
        double avg = storyPointsLastThreeSprints.stream().mapToInt(i -> i).average().orElse(0);
        System.out.println("last 3 sprint points: " + storyPointsLastThreeSprints);
        System.out.println("average velocity (story points / sprint): " + String.format("%.1f", avg));
    }

    public static void main(String[] args) {
        demo();
    }
}
