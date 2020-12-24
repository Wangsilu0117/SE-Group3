package me.bakumon.moneykeeper.ui.storetyperecords;

        import java.util.Date;
        import java.util.List;

        import io.reactivex.Completable;
        import io.reactivex.Flowable;
        import me.bakumon.moneykeeper.base.BaseViewModel;
        import me.bakumon.moneykeeper.database.entity.RecordWithType;
        import me.bakumon.moneykeeper.datasource.AppDataSource;
        import me.bakumon.moneykeeper.ui.typerecords.TypeRecordsFragment;
        import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 某一类型的记账记录
 *
 * @author Bakumon https://bakumon.me
 */
public class StoreTypeRecordsViewModel extends BaseViewModel {
    public StoreTypeRecordsViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordWithType>> getStoreRecordWithTypesSortMoney(int sortType, int type, int typeId, int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        if (sortType == StoreTypeRecordsFragment.SORT_TIME) {
            return mDataSource.getStoreRecordWithTypesSortMoney(dateFrom, dateTo, type, typeId);
        } else {
            return mDataSource.getStoreRecordWithTypesSortMoney(dateFrom, dateTo, type, typeId);
        }
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }


}

