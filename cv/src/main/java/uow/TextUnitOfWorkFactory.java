package uow;

public class TextUnitOfWorkFactory implements IUnitOfWorkFactory {
    @Override
    public IUnitOfWork createUnitOfWork() {
        return new TextUnitOfWork();
    }
}