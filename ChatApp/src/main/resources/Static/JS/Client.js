var Socket = io ();
import React from 'react';
import ReactDOM from 'react-dom';
import '../CSS/Style.css';

function Square(props) {
  return (
    <button className="square" onClick={props.onClick}>
      {props.value}
    </button>
  );
}

class Board extends React.Component {
  renderSquare(i) {
    return (
      <Square
        value={this.props.squares[i]}
        onClick={() => this.props.onClick(i)}
      />
    );
  }

  render() {
    return (
      <div>
        <div className="board-row">
          {this.renderSquare(0)}
          {this.renderSquare(1)}
          {this.renderSquare(2)}
        </div>
        <div className="board-row">
          {this.renderSquare(3)}
          {this.renderSquare(4)}
          {this.renderSquare(5)}
        </div>
        <div className="board-row">
          {this.renderSquare(6)}
          {this.renderSquare(7)}
          {this.renderSquare(8)}
        </div>
      </div>
    );
  }
}

class Game extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      history: [{
        squares: Array(9).fill(null),
      }],
      xIsNext: true,
      pos: 0,
    };
  }

  jumpTo(step) {
    this.setState({
      pos: step,
      xIsNext: (step % 2) === 0,
    });
  }

  clearBoard() {
    this.setState({
      history: [{
        squares: Array(9).fill(null),
      }],
      xIsNext: true,
      pos: 0,
    });
  }

  handleClick(i) {
    const history = this.state.history.slice(0, this.state.pos + 1);
    const current = history[this.state.pos];
    const squares = current.squares.slice();
    if (calculateWinner(squares) || squares[i]) {
      return;
    }
    squares[i] = this.state.xIsNext ? 'X' : 'O';
    this.setState({
      history: history.concat([{
        squares: squares,
      }]),
      xIsNext: !this.state.xIsNext,
      pos: this.state.pos + 1,
    });
  }

  render() {
    const history = this.state.history;
    const current = history[this.state.pos];
    const winner = calculateWinner(current.squares);

    const pos = this.state.pos;
    const undo = <button onClick={() => this.jumpTo(pos > 0 ? pos - 1 : pos)} className="medium-btn">Undo</button>
    const redo = <button onClick={() => this.jumpTo(pos < history.length - 1 ? pos + 1 : pos)} className="medium-btn">Redo</button>
    const clear = <button onClick={() => this.clearBoard()} className="medium-btn">Clear</button>

    let status;
    if (winner) {
      status = `${winner} Wins!`;
    } else if (!current.squares.includes(null)) {
      status = 'Tie!';
    } else {
      status = `${(this.state.xIsNext ? 'X' : 'O')}'s Turn!`;
    }

    return (
      <div>
        <div className="game">
          <div className="game-board">
            <Board
              squares={current.squares}
              onClick={(i) => this.handleClick(i)}
            />
          </div>
          <div className="game-info">
            <div>{status}</div>
            <div>{undo}</div>
            <div>{redo}</div>
            <div>{clear}</div>
          </div>
        </div>
      </div>
    );
  }
}

function calculateWinner(squares) {
  const lines = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [2, 4, 6],
  ];
  for (let i = 0; i < lines.length; i++) {
    const [a, b, c] = lines[i];
    if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
      return squares[a];
    }
  }
  return null;
}

// ========================================

ReactDOM.render(
  <Game />,
  document.getElementById('root')
);
