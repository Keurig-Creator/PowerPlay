package org.example;



import java.util.Scanner;

public class Main {
    private boolean running = true;

    public static void main(String[] args) {
        new Main().start();
    }

    public enum LinearState {
        LIFT_START,
        LIFT_EXTEND,
        DUMP,
        LIFT_EXTRACT,
    }

    LinearState linearState = LinearState.LIFT_START;

    ElapsedTime liftTime = new ElapsedTime();

    final double DUMP_IDLE = 0; // the idle position for the dump servo
    final double DUMP_DEPOSIT = 1; // the dumping position for the dump servo

    // the amount of time the dump servo takes to activate in seconds
    final double DUMP_TIME = 2;

    final int LIFT_LOW = 50; // the low encoder position for the lift
    final int LIFT_HIGH = 1000; // the high encoder position for the lift

    Scanner scanner = new Scanner(System.in);

    public void start() {
        liftTime.reset();

        int motorEncoder = 50;

        while (running) {
            String line = scanner.nextLine();

            if (linearState == LinearState.LIFT_EXTEND && motorEncoder <= LIFT_HIGH) {
                motorEncoder++;
            }

            if (linearState == LinearState.LIFT_EXTRACT && motorEncoder >= LIFT_LOW) {
                motorEncoder--;
            }

            switch (linearState) {
               case LIFT_START:
                   if (line.equalsIgnoreCase("x")) {
                       System.out.println("Lift Start -> Lift Extend");
                       motorEncoder = 51;
                       updateState(LinearState.LIFT_EXTEND);
                   }
                   break;
               case LIFT_EXTEND:
                    if (motorEncoder - LIFT_HIGH < 10) {

                        System.out.println("Lift Extended -> Dump");

                        liftTime.reset();
                        updateState(LinearState.DUMP);
                    }
                   break;
               case DUMP:
                    if (liftTime.seconds() >= DUMP_TIME) {

                        System.out.println("Dump -> Extract");
                        updateState(LinearState.LIFT_EXTRACT);
                    }
                   break;
               case LIFT_EXTRACT:
                    if (motorEncoder - LIFT_LOW < 10) {
                        System.out.println("Lift Extract -> Lift Start");
                        updateState(LinearState.LIFT_START);
                    }
                   break;
               default:
                   updateState(LinearState.LIFT_START);
           }

            // small optimization, instead of repeating ourselves in each
            // lift state case besides LIFT_START for the cancel action,
            // it's just handled here
            if (line.equalsIgnoreCase("y") && linearState != LinearState.LIFT_START) {
                updateState(LinearState.LIFT_START);
            }
        }
    }


    public void updateState(LinearState state) {
        System.out.println("CURRENT STATE = " + state.name());
        linearState = state;
    }

}