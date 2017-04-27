package blindfoldchesstrainer.engine.player;

/**
 * Created by Anton on 1/24/2017.
 */
public enum MoveStatus {
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },

    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    },

    LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isDone() {
            return false;
        }
    };

    public abstract boolean isDone();
}
