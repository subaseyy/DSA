package main.task3;


import java.util.ArrayList;
import java.util.List;

// Question 3, a
public class smallCommunity {
    // Define the Union-Find data structure
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return false;
            }
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }

        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }

    public static List<String> processFriendRequests(int n, int[][] restrictions, int[][] requests) {
        UnionFind uf = new UnionFind(n);
        List<String> results = new ArrayList<>();

        for (int[] request : requests) {
            int house1 = request[0];
            int house2 = request[1];
            boolean canBeFriends = true;


            for (int[] restriction : restrictions) {
                int restrictedHouse1 = restriction[0];
                int restrictedHouse2 = restriction[1];
                if ((uf.isConnected(house1, restrictedHouse1) && uf.isConnected(house2, restrictedHouse2)) ||
                        (uf.isConnected(house1, restrictedHouse2) && uf.isConnected(house2, restrictedHouse1))) {
                    canBeFriends = false;
                    break;
                }
            }


            if (canBeFriends) {
                uf.union(house1, house2);
                results.add("approved");
            } else {

                results.add("denied");
            }
        }

        return results;
    }
    public static void main(String[] args) {
        int n = 5;
        int[][] restrictions = {{0, 1}, {1, 2}, {2, 3}};
        int[][] requests = {{0, 4}, {1, 2}, {3, 1}, {3, 4}};
        List<String> result = processFriendRequests(n, restrictions, requests);
        for (String res : result) {
            System.out.println(res);
       }
    }
}
