package blindfoldchesstrainer.engine.pieces;

import blindfoldchesstrainer.engine.Alliance;
import blindfoldchesstrainer.engine.board.Board;
import blindfoldchesstrainer.engine.board.BoardUtils;
import blindfoldchesstrainer.engine.board.Move;
import blindfoldchesstrainer.engine.board.Move.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anton on 1/20/2017.
 */
public class Pawn extends Piece {

    private final static int[] CANDIDATE_LEGAL_MOVE_OFFSETS = {8, 16, 7, 9};

    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_LEGAL_MOVE_OFFSETS) {
            int candidateDestinationCoordinate = this.piecePosition + this.getPieceAlliance().getDirection() *
                    currentCandidateOffset;

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnPromotion(new MajorMove(board, getPromotionQueen(), candidateDestinationCoordinate)));
                    legalMoves.add(new PawnPromotion(new MajorMove(board, getPromotionRook(), candidateDestinationCoordinate)));
                    legalMoves.add(new PawnPromotion(new MajorMove(board, getPromotionKnight(), candidateDestinationCoordinate)));
                    legalMoves.add(new PawnPromotion(new MajorMove(board, getPromotionBishop(), candidateDestinationCoordinate)));
                }
                else
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
            }

            else if(currentCandidateOffset == 16 && this.isFirstMove &&
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                     (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            }

            else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                     (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidateDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceOnCandidateDestination.pieceAlliance != this.pieceAlliance) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new AttackMove(board, getPromotionQueen(), candidateDestinationCoordinate, pieceOnCandidateDestination)));
                            legalMoves.add(new PawnPromotion(new AttackMove(board, getPromotionRook(), candidateDestinationCoordinate, pieceOnCandidateDestination)));
                            legalMoves.add(new PawnPromotion(new AttackMove(board, getPromotionKnight(), candidateDestinationCoordinate, pieceOnCandidateDestination)));
                            legalMoves.add(new PawnPromotion(new AttackMove(board, getPromotionBishop(), candidateDestinationCoordinate, pieceOnCandidateDestination)));
                        }
                        else
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidateDestination));
                    }
                }
                else if(board.getEnPassantPawn() != null) {
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }

            else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                     (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidateDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (pieceOnCandidateDestination.pieceAlliance != this.pieceAlliance) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new AttackMove(board, getPromotionQueen(), candidateDestinationCoordinate, pieceOnCandidateDestination)));
                            legalMoves.add(new PawnPromotion(new AttackMove(board, getPromotionRook(), candidateDestinationCoordinate, pieceOnCandidateDestination)));
                            legalMoves.add(new PawnPromotion(new AttackMove(board, getPromotionKnight(), candidateDestinationCoordinate, pieceOnCandidateDestination)));
                            legalMoves.add(new PawnPromotion(new AttackMove(board, getPromotionBishop(), candidateDestinationCoordinate, pieceOnCandidateDestination)));
                        }
                        else
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidateDestination));
                    }
                }
                else if(board.getEnPassantPawn() != null) {
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }

        return Collections.unmodifiableCollection(legalMoves);
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionQueen() {
        return new Queen(this.pieceAlliance, this.piecePosition, false);
    }
    
    public Piece getPromotionRook() {
        return new Rook(this.pieceAlliance, this.piecePosition, false);
    }
    
    public Piece getPromotionKnight() {
        return new Knight(this.pieceAlliance, this.piecePosition, false);
    }
    
    public Piece getPromotionBishop() {
        return new Bishop(this.pieceAlliance, this.piecePosition, false);
    }
    
    @Override
    public int positionBonus() {
        return getPieceAlliance().pawnBonus(this.piecePosition);
    }
}
