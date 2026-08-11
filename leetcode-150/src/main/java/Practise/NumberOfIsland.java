package Practise;

public class NumberOfIsland {
    public void dfs(char[][] grid, int m, int n) {

        if(m < 0 ||
                m >= grid.length ||
                n < 0 ||
                n >= grid[0].length ||
                grid[m][n] == '0')
        {
            return;
        }

        grid[m][n] = '0';

        dfs(grid, m + 1, n);
        dfs(grid, m - 1, n);
        dfs(grid, m, n + 1);
        dfs(grid, m, n - 1);
    }

    public int numIslands(char[][] grid) {

        int m = grid.length;
        int n = grid[0].length;

        int count = 0;

        for(int i = 0; i < m; i++) {

            for(int j = 0; j < n; j++) {

                if(grid[i][j] == '1') {

                    count++;

                    dfs(grid, i, j);
                }
            }
        }

        return count;
    }
    public static void main(String[] args) {

        char[][] grid = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };

        NumberOfIsland obj = new NumberOfIsland();

        int result = obj.numIslands(grid);

        System.out.println("Number of Islands = " + result);
    }

}
