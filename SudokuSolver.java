import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolver extends JFrame {

    private JTextField[][] grid;
    private Image backgroundImage;

    public SudokuSolver() {
        setTitle("Sudoku Solver");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        try {
            backgroundImage = new ImageIcon("background.jpg").getImage();
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            backgroundImage = null;
        }

        
        JPanel panel = new BackgroundPanel();
        panel.setLayout(new GridLayout(9, 9));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));  

        grid = new JTextField[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row][col] = new JTextField();
                grid[row][col].setHorizontalAlignment(JTextField.CENTER);
                grid[row][col].setPreferredSize(new Dimension(50, 50));  
                panel.add(grid[row][col]);
            }
        }

        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });

        add(panel, BorderLayout.CENTER);
        add(solveButton, BorderLayout.SOUTH);
    }

    private void solveSudoku() {
        int[][] board = new int[9][9];
        boolean validInput = true;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                try {
                    String text = grid[row][col].getText();
                    board[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
                    if (board[row][col] < 0 || board[row][col] > 9) {
                        validInput = false;
                        break;
                    }
                } catch (NumberFormatException e) {
                    validInput = false;
                    break;
                }
            }
            if (!validInput) break;
        }

        if (!validInput) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numbers between 1 and 9 only.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (solve(board)) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    grid[row][col].setText(Integer.toString(board[row][col]));
                    setCellColor(grid[row][col], Color.BLUE); 
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setCellColor(JTextField cell, Color color) {
        cell.setForeground(color);
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
      
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num || board[x][col] == num ||
                board[row - row % 3 + x / 3][col - col % 3 + x % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean solve(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) {
                                return true;
                            }
                            board[row][col] = 0; 
                        }
                    }
                    return false; 
                }
            }
        }
        return true; 
    }

    
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuSolver solver = new SudokuSolver();
            solver.setVisible(true);
        });
    }
}
