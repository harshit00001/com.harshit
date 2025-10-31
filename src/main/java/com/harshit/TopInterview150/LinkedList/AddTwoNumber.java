package com.harshit.TopInterview150.LinkedList;



public class AddTwoNumber {

        public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0); // dummy head
            ListNode current = dummy;
            int carry = 0;

            while (l1 != null || l2 != null || carry != 0) {
                int sum = carry;

                if (l1 != null) {
                    sum += l1.val;
                    l1 = l1.next;
                }

                if (l2 != null) {
                    sum += l2.val;
                    l2 = l2.next;
                }

                carry = sum / 10;
                current.next = new ListNode(sum % 10);
                current = current.next;
            }

            return dummy.next;
        }

        // Helper method to create a linked list from array
        public static ListNode createList(int[] digits) {
            ListNode dummy = new ListNode(0);
            ListNode current = dummy;
            for (int digit : digits) {
                current.next = new ListNode(digit);
                current = current.next;
            }
            return dummy.next;
        }

        // Helper method to print a linked list
        public static void printList(ListNode head) {
            while (head != null) {
                System.out.print(head.val);
                if (head.next != null) System.out.print(" -> ");
                head = head.next;
            }
            System.out.println();
        }

        public static void main(String[] args) {
            // Example 1
            ListNode l1 = createList(new int[]{2, 4, 3});
            ListNode l2 = createList(new int[]{5, 6, 4});
            ListNode result1 = addTwoNumbers(l1, l2);
            System.out.print("Output 1: ");
            printList(result1); // 7 -> 0 -> 8

            // Example 2
            ListNode l3 = createList(new int[]{0});
            ListNode l4 = createList(new int[]{0});
            ListNode result2 = addTwoNumbers(l3, l4);
            System.out.print("Output 2: ");
            printList(result2); // 0

            // Example 3
            ListNode l5 = createList(new int[]{9,9,9,9,9,9,9});
            ListNode l6 = createList(new int[]{9,9,9,9});
            ListNode result3 = addTwoNumbers(l5, l6);
            System.out.print("Output 3: ");
            printList(result3); // 8 -> 9 -> 9 -> 9 -> 0 -> 0 -> 0 -> 1
        }
    }
