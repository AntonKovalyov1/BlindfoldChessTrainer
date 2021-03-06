package blindfoldchesstrainer.engine.board;

import blindfoldchesstrainer.engine.pieces.Bishop;
import blindfoldchesstrainer.engine.pieces.Piece;
import blindfoldchesstrainer.engine.Alliance;
import blindfoldchesstrainer.engine.pieces.King;
import blindfoldchesstrainer.engine.pieces.Knight;
import blindfoldchesstrainer.engine.pieces.Pawn;
import blindfoldchesstrainer.engine.pieces.Queen;
import blindfoldchesstrainer.engine.pieces.Rook;
import blindfoldchesstrainer.engine.player.BlackPlayer;
import blindfoldchesstrainer.engine.player.Player;
import blindfoldchesstrainer.engine.player.PlayerType;
import blindfoldchesstrainer.engine.player.WhitePlayer;

import java.util.*;

/**
 * Created by Anton on 1/17/2017.
 */
public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final Pawn enPassantPawn;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves, builder.whitePlayerType);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves, builder.blackPlayerType);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableCollection(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,
                                                    final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile: gameBoard) {
            if(tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }

        return Collections.unmodifiableCollection(activePieces);
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));
        }
        return Collections.unmodifiableList(tiles);
    }

    public static Builder createStandardBoardBuilder() {
        final Builder builder = new Builder();
        // Black
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        // White
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        // White to move
        builder.setMoveMaker(Alliance.WHITE);

        return builder;
    }

    public static Board createStandardBoard() {
        return createStandardBoardBuilder().build();
    }

    public static Board createStandardGameBoard(PlayerType whitePlayerType, PlayerType blackPlayerType) {
        final Builder builder = createStandardBoardBuilder();
        builder.setWhitePlayerType(whitePlayerType);
        builder.setBlackPlayerType(blackPlayerType);

        return builder.build();
    }

    public Collection<Move> getAllLegalMoves() {
        List<Move> allLegalMoves = new ArrayList<>(this.whitePlayer.getLegalMoves());
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
        return Collections.unmodifiableCollection(allLegalMoves);
    }

    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;
        PlayerType whitePlayerType = PlayerType.HUMAN;
        PlayerType blackPlayerType = PlayerType.HUMAN;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
                return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        public void setWhitePlayerType(PlayerType whitePlayerType) {
            this.whitePlayerType = whitePlayerType;
        }

        public void setBlackPlayerType(PlayerType blackPlayerType) {
            this.blackPlayerType = blackPlayerType;
        }
    }
}
