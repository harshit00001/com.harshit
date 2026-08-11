package com.learning.threads.synchronization;

public class BookmovieTicket {
    public static void main(String[] args) {
        BookSeat bk = new BookSeat();
        CreateTicket createTicket = new CreateTicket(bk,8);
        createTicket.setName("harshit");
        createTicket.start();
        CreateTicket createTicket2 = new CreateTicket(bk,9);
        createTicket2.setName("Rahul");
        createTicket2.start();
    }

}
class BookSeat
{
    int seats =10;
    public synchronized void bookSeat(int numberOfSeat, String name)
    {
        if(seats>=numberOfSeat)
        {
            System.out.println(seats + " seats available for this person: " + name);

            seats = seats-numberOfSeat;

            System.out.println(seats + " remaining seats available for next person ");
        }
        else
        {
            System.out.println("Seats are unavailable");
        }
    }
}
class CreateTicket extends Thread
{
    int seats;
    BookSeat bs;

    CreateTicket(BookSeat bs, int seats)
    {
        this.bs = bs;
        this.seats= seats;
    }

    @Override
    public void run() {
        bs.bookSeat(seats,Thread.currentThread().getName());

    }
}

