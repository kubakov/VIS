package uow;

public interface IUnitOfWorkFactory {
    IUnitOfWork createUnitOfWork();
}