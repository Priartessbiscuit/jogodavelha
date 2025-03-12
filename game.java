public class Game {
    private char[][] board;
    private char currentPlayer;

    public Game() {
        board = new char[3][3];
        currentPlayer = 'X';  // Jogador começa com 'X'
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean playMove(int row, int col) {
        if (board[row][col] == 0) {
            board[row][col] = currentPlayer;
            if (checkWin()) {
                return true;
            }
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            return true;
        }
        return false;
    }

    private boolean checkWin() {
        // Checar linhas, colunas e diagonais
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) || 
                (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer)) {
                return true;
            }
        }
        if ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) || 
            (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)) {
            return true;
        }
        return false;
    }

    // Verifica se o jogo terminou (ganho ou empate)
    public boolean isGameOver() {
        return checkWin() || isBoardFull();
    }

    // Checa se o tabuleiro está cheio (empate)
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Função de avaliação para o Minimax
    public int evaluate() {
        if (checkWin()) {
            return (currentPlayer == 'O') ? 1 : -1; // 'O' ganha é +1, 'X' ganha é -1
        }
        return 0; // Empate
    }

    // Função Minimax
    public int minimax(int depth, boolean isMaximizingPlayer) {
        int score = evaluate();

        // Se o jogo terminou
        if (score == 1 || score == -1) {
            return score;
        }
        if (isBoardFull()) {
            return 0; // Empate
        }

        // Se for a vez do computador (Maximizar)
        if (isMaximizingPlayer) {
            int best = -1000;
            // Tenta todas as jogadas possíveis
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = 'O'; // 'O' é o computador
                        best = Math.max(best, minimax(depth + 1, false));
                        board[i][j] = 0;
                    }
                }
            }
            return best;
        }
        // Se for a vez do jogador (Minimizar)
        else {
            int best = 1000;
            // Tenta todas as jogadas possíveis
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = 'X'; // 'X' é o jogador
                        best = Math.min(best, minimax(depth + 1, true));
                        board[i][j] = 0;
                    }
                }
            }
            return best;
        }
    }

    // Função para encontrar a melhor jogada para o computador
    public int[] bestMove() {
        int bestVal = -1000;
        int[] bestMove = new int[]{-1, -1};

        // Tenta todas as jogadas possíveis
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = 'O'; // 'O' é o computador
                    int moveVal = minimax(0, false);
                    board[i][j] = 0;

                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }
}
@WebServlet("/game")
public class GameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Game game;

    public GameServlet() {
        super();
        game = new Game();  // Inicializa a lógica do jogo
    }

    // Processa as requisições GET (informações do jogo)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        // Retorna o estado atual do jogo (tabuleiro e próximo jogador)
        out.println("{\"board\":" + Arrays.deepToString(game.getBoard()) + ",\"currentPlayer\":\"" + game.getCurrentPlayer() + "\"}");
    }

    // Processa as requisições POST (movimento do jogador e IA)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lê os parâmetros da requisição (movimento do jogador)
        int row = Integer.parseInt(request.getParameter("row"));
        int col = Integer.parseInt(request.getParameter("col"));
        
        // Jogador faz a jogada
        boolean validMove = game.playMove(row, col);
        
        if (validMove && !game.isGameOver()) {
            // Se o jogo não acabou, faz a jogada da IA (computador)
            int[] bestMove = game.bestMove();
            game.playMove(bestMove[0], bestMove[1]);
        }
        
        // Retorna o estado do jogo após o movimento do jogador e da IA
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println("{\"board\":" + Arrays.deepToString(game.getBoard()) + ",\"currentPlayer\":\"" + game.getCurrentPlayer() + "\"}");
    }
}

    