package org.bearmug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.IntStream;

public class Console {

    public static void main(String[] args) {
        new Console().process(args.length > 0);
    }

    private void process(boolean interop) {

        final BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        RoutingEngine engine = initEngine(interop, reader);
        if (engine == null) {
            return;
        }
        do {
            System.out.println("Ready to solve next problem. Type 'x' to exit.");
        } while (solve(engine, reader));
    }

    private RoutingEngine initEngine(boolean interop, BufferedReader reader) {

        int legsNumber = -1;
        System.out.println(
                "####################################################\n" +
                        "# Welcome to route calculation app! This app able to:\n" +
                        "# - find best route for you\n" +
                        "# - lookup for closest points\n" +
                        "# Please go ahead with initial data input here.\n" +
                        "####################################################\n");
        while (legsNumber < 0) {
            System.out.println(
                    "Type edges number please or 'x' to exit: ");
            try {
                String data = reader.readLine();
                if (data.trim().equalsIgnoreCase("x")) {
                    return null;
                }
                legsNumber = Integer.parseInt(data);
            } catch (IOException | NumberFormatException e) {
                System.out.println(">>>Read data error. Please try again");
            }
        }
        System.out.println("Please go ahead with edges input. The format is:\n" +
                "\n" +
                "<source> -> <destination>: <travel time>");
        RouteLeg[] legs = IntStream.range(0, legsNumber)
                .mapToObj(i -> produceLeg(reader))
                .filter(l -> l != null)
                .toArray(RouteLeg[]::new);

        return interop ?
                RoutingEngine.interop(legs) : RoutingEngine.classic(legs);
    }

    private boolean solve(RoutingEngine engine, BufferedReader reader) {
        try {

            String data = reader.readLine().trim();
            if (data.equalsIgnoreCase("x")) {
                return false;
            } else if (data.startsWith("route")) {
                String[] input = data.split("(route|->)");
                System.out.println(engine.route(input[1].trim(), input[2].trim()));
            } else if (data.startsWith("nearby")) {
                String[] input = data.split("(nearby|,)");
                System.out.println(engine.nearby(input[1].trim(), Long.parseLong(input[2].trim())));
            } else {
                throw new IllegalStateException("Please");
            }

        } catch (IOException
                | ArrayIndexOutOfBoundsException
                | NumberFormatException
                | IllegalStateException e) {
            System.out.println(">>>Problem solution error: " + e.getMessage());
        }
        return true;
    }

    private RouteLeg produceLeg(BufferedReader reader) {
        try {
            String[] data = reader.readLine().split("(->|:)");
            return new RouteLeg(data[0].trim(), data[1].trim(), Long.parseLong(data[2].trim()));
        } catch (IOException
                | ArrayIndexOutOfBoundsException
                | NumberFormatException e) {
            System.out.println(">>>Edge read error. Be aware about possible routing issues.");
        }
        return null;
    }
}