package main.task4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


// Question 4, a
public class cityPlanner {
    static class Edge {
        int to, weight;
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] roads = {
                {4, 1, -1},
                {2, 0, -1},
                {0, 3, -1},
                {4, 3, -1}
        };
        int source = 0;
        int destination = 1;
        int targetTime = 5;

        List<int[]> result = findValidModification(n, roads, source, destination, targetTime);
        for (int[] road : result) {
            System.out.println(Arrays.toString(road));
        }
    }

    public static List<int[]> findValidModification(int n, int[][] roads, int source, int destination, int targetTime) {
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        List<int[]> underConstruction = new ArrayList<>();

        for (int[] road : roads) {
            if (road[2] == -1) {
                underConstruction.add(road);
            } else {
                graph.get(road[0]).add(new Edge(road[1], road[2]));
                graph.get(road[1]).add(new Edge(road[0], road[2]));
            }
        }

        for (int[] road : underConstruction) {
            road[2] = 1;
            graph.get(road[0]).add(new Edge(road[1], road[2]));
            graph.get(road[1]).add(new Edge(road[0], road[2]));
        }

        int initialDistance = dijkstra(graph, source, destination, n);

        if (initialDistance == targetTime) {
            return Arrays.asList(roads);
        } else {
            int extraTime = targetTime - initialDistance;
            for (int[] road : underConstruction) {
                if (extraTime > 0) {
                    road[2] += extraTime;
                    graph.get(road[0]).clear();
                    graph.get(road[1]).clear();
                }
            }
            for (int[] road : roads) {
                graph.get(road[0]).add(new Edge(road[1], road[2]));
                graph.get(road[1]).add(new Edge(road[0], road[2]));
            }

            return Arrays.asList(roads);
        }
    }

    public static int dijkstra(List<List<Edge>> graph, int source, int destination, int n) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.weight));
        pq.add(new Edge(source, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = current.to;
            int d = current.weight;

            if (d > dist[u]) continue;

            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int weight = edge.weight;
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new Edge(v, dist[v]));
                }
            }
        }
        return dist[destination];
    }
    
}
