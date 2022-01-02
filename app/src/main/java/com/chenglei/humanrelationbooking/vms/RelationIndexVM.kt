package com.chenglei.humanrelationbooking.vms

import androidx.annotation.Keep
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.base.arch.FlowCollectDataSource
import com.chenglei.humanrelationbooking.base.arch.newFetch
import com.chenglei.humanrelationbooking.books.create.RelationDataSource
import com.chenglei.humanrelationbooking.getString
import com.chenglei.humanrelationbooking.io
import com.chenglei.humanrelationbooking.utils.toast
import com.hangshun.huadian.android.common.arch.BaseViewModel
import com.hangshun.huadian.android.common.meta.ParamAndResult
import com.hangshun.huadian.android.common.network.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.Serializable

class RelationIndexVM:BaseViewModel(){
    val count = MutableStateFlow(0)
    val pages = MutableStateFlow(arrayListOf(getString(R.string.relation_all)).apply {
        addAll(RelationDataSource().relationConfigs())
    })

    private val fetcher by lazy {
        RelationTypeFetcher(viewModelScope)
    }

    val newTypeDialogShow = MutableStateFlow(false)
    val newRelationDialogShow = MutableStateFlow(false)

    private val pagerViewModelStores = mutableMapOf<Int, ViewModelStore>()

    fun getPagerViewModelStore(page:Int) : ViewModelStoreOwner {
        var viewModelStore = pagerViewModelStores[page]
        if (viewModelStore == null) {
            viewModelStore = ViewModelStore()
            pagerViewModelStores[page] = viewModelStore
        }
       return  ViewModelStoreOwner {
            viewModelStore
        }
    }

    override fun onCleared() {
        pagerViewModelStores.values.forEach(ViewModelStore::clear)
        pagerViewModelStores.clear()
        super.onCleared()
    }

    fun count() {
       io {
//           BmobQuery<RelationItem>().addWhereEqualTo("owner", BmobUser.getCurrentUser().objectId)
//               .count(RelationItem::class.java, object : CountListener() {
//                   override fun done(count: Int?, e: BmobException?) {
//                       if (e == null) {
//                           count?.apply {
//                               this@RelationIndexVM.count.value = this
//                           }
//                       }
//                   }
//               })
       }
    }

    fun createNewRelation(newRelation:String) {
        io {
//            if (newRelation.isEmpty()) return@io
//            val query = BmobQuery<RelationType>()
//            query.addWhereEqualTo("owner", BmobUser.getCurrentUser().objectId)
//                .addWhereEqualTo("relation", newRelation)
//            query.count(RelationType::class.java, object : CountListener() {
//                override fun done(count: Int?, e: BmobException?) {
//                    e?.let {
//                        toast(R.string.toast_save_fail)
//                    }?: kotlin.run {
//                        count?.let {
//                            if (it > 0) {
//                                toast(String.format(getString(R.string.relation_exist), newRelation))
//                            } else {
//                                // 保存新的
//                                val newRelationType = RelationType(newRelation, BmobUser.getCurrentUser().objectId)
//                                newRelationType.save(object : SaveListener<String>() {
//                                    override fun done(objectId: String?, e: BmobException?) {
//                                        e?.let {
//                                            toast(R.string.toast_save_fail)
//                                        }?: kotlin.run {
//                                            toast(R.string.toast_save_success)
//                                            this@RelationIndexVM.newRelationDialogShow.value = false
//                                            addRelation(newRelation)
//                                        }
//                                    }
//                                })
//                            }
//                        } ?: kotlin.run {
//                            toast(R.string.toast_save_fail)
//                        }
//                    }
//                }
//            })
        }
    }

    fun fetchRelationTypes() {
        fetcher.fetch {
            this.pages.value = it
        }
    }

    fun addRelation(newRelation: String) {
        val list = this.pages.value.apply { add(newRelation) }
        this.pages.value = arrayListOf<String>().apply {
            addAll(list)
        }
    }
}

class RelationTypeFetcher(private val scope:CoroutineScope) {
    fun fetch(callback: (ArrayList<String>)->Unit) {
        scope.launch(IO) {
//            BmobQuery<RelationType>()
//                .addWhereEqualTo("owner", BmobUser.getCurrentUser().objectId)
//                .findObjects(object : FindListener<RelationType>(){
//                    override fun done(relations: MutableList<RelationType>?, e: BmobException?) {
//                        e?.let {  } ?: run {
//                            relations?.let {relations->
//                                val relations = relations.map { it.relation }
//                                callback.invoke(
//                                    arrayListOf(getString(R.string.all)).apply {
//                                        addAll(RelationDataSource().relationConfigs())
//                                        addAll(relations)
//                                    }
//                                )
//                            }
//                        }
//                    }
//                } )
        }
    }
}


@Keep
data class RelationType(
    val relation: String,
    val owner :String
)

@Keep
data class RelationItem(
    val name:String,
    val relation :String,
    val phone:String,
    val remark:String,
    val owner:String,
    var income:Int = 0,
    var spend:Int = 0
):Serializable

fun RelationItem.income(money:Int) {
    this.income += money
}

fun RelationItem.spend(money:Int) {
    this.spend += money
}

fun RelationItem.new():RelationItem {
    return RelationItem(this.name, this.relation, this.phone,
    this.remark, this.owner, this.income, this.spend)
}