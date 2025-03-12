@WebServlet("/game")
public class GameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Game game;

    public GameServlet() {
        super();
        game = new Game();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Aqui você pode pegar o estado do jogo e enviar para o frontend
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println("{\"currentPlayer\": \"" + game.getCurrentPlayer() + "\"}");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Aqui você processa as jogadas feitas pelo usuário
        int row = Integer.parseInt(request.getParameter("row"));
        int col = Integer.parseInt(request.getParameter("col"));
        
        if (game.playMove(row, col)) {
            response.getWriter().write("OK");
        } else {
            response.getWriter().write("Invalid Move");
        }
    }
}
