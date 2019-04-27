package calemiutils.tileentity.base;

public interface IProgress {

    int getCurrentProgress();

    @SuppressWarnings("SameReturnValue")
    int getMaxProgress();
}
