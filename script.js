let currentPlayer = "X";  // Começa com o jogador X

// Função para simular a jogada do computador
function makeComputerMove() {
    // Espera que a função bestMove do Java retorna a melhor jogada
    let move = getBestMoveFromServer(); // Isso seria uma chamada para o backend
    let cells = document.querySelectorAll(".board div");
    if (cells[move[0] * 3 + move[1]].textContent === "") {
        cells[move[0] * 3 + move[1]].textContent = "O"; // Computador joga com O
        checkGameOver();
    }
}

// Exemplo de como poderia ser uma chamada para o backend (Servlet Java)
function getBestMoveFromServer() {
    // Fazer chamada AJAX para o backend em Java que retorna a melhor jogada
    // Retorne o movimento calculado pela IA (ex: [1, 2] significa linha 1, coluna 2)
    return [1, 1]; // Esta é apenas uma simulação
}

// Depois de o jogador X fazer sua jogada, o computador fará a dele
function playerMove(index) {
    // O código para fazer a jogada do jogador X
    checkGameOver();
    makeComputerMove();  // Após o jogador, o computador faz a jogada
}

