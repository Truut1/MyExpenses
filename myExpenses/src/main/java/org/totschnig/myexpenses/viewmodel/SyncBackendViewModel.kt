package org.totschnig.myexpenses.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.annimon.stream.Collectors
import com.annimon.stream.Exceptional
import com.squareup.sqlbrite3.SqlBrite
import kotlinx.coroutines.Dispatchers
import org.totschnig.myexpenses.MyApplication
import org.totschnig.myexpenses.provider.DatabaseConstants
import org.totschnig.myexpenses.provider.TransactionProvider
import org.totschnig.myexpenses.sync.GenericAccountService
import org.totschnig.myexpenses.sync.SyncBackendProviderFactory
import org.totschnig.myexpenses.sync.json.AccountMetaData
import java.util.*

class SyncBackendViewModel(application: Application) : AbstractSyncBackendViewModel(application) {

    override fun getAccounts(context: Context) = GenericAccountService.getAccountNamesWithEncryption(context)

    override fun accountMetadata(accountName: String): LiveData<Exceptional<List<AccountMetaData>>> = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(SyncBackendProviderFactory.get(getApplication<MyApplication>(), GenericAccountService.GetAccount(accountName), false).map { it.remoteAccountList.collect(Collectors.toList()) })
    }

}