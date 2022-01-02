package com.chenglei.humanrelationbooking.books.create

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.chenglei.humanrelationbooking.ActivityConst
import com.chenglei.humanrelationbooking.R
import com.chenglei.humanrelationbooking.meta.Book
import com.chenglei.humanrelationbooking.meta.BookItem
import com.chenglei.humanrelationbooking.vms.RelationItem
import com.hangshun.huadian.android.common.arch.ActivityBase
import com.hangshun.huadian.android.common.arch.ToolbarConfig
import com.hangshun.huadian.android.common.arch.handleStatusBar
import com.hangshun.huadian.android.common.arch.toolbarConfig

class RecordCreateActivity:ActivityBase() {
    override fun needToolBar(): Boolean {
        return true
    }

    override fun toolBarConfig(): ToolbarConfig {
        return toolbarConfig {
            color = R.color.white
            title = getString(if (intent.getBundleExtra(ActivityConst.KEY_BUNDLE_BOOKS)?.getInt(ActivityConst.KEY_Type)?:ActivityConst.Const_Create == ActivityConst.Const_Create) R.string.create_humanrelation else R.string.edit_humanrelation)
            textColor = R.color.color_262626
            navigatorColor = R.color.color_666666
        }
    }

    override fun realContentView(): View {
       return LayoutInflater.from(this).inflate(R.layout.activity_books_create, getRoot(), false)
    }

    override fun initViews() {
        val bundle = intent.getBundleExtra(ActivityConst.KEY_BUNDLE_BOOKS)
        supportFragmentManager.findFragmentById(R.id.fragmentBookContentCreate)
            ?.arguments = bundle
    }


    companion object {
        fun launchCreate(context:Context, books:List<Book> = emptyList(), currentBook:Book?=null,
        currentRelation:RelationItem? = null) {
            context.startActivity(
                Intent(context, RecordCreateActivity::class.java).apply {
                    putExtra(ActivityConst.KEY_BUNDLE_BOOKS, bundleOf(
                        ActivityConst.KEY_Type to ActivityConst.Const_Create,
                        ActivityConst.KEY_BOOKS to books,
                        ActivityConst.Key_Current_Book to currentBook,
                        ActivityConst.Key_Current_Relation to currentRelation
                    ))
                }
            )
        }

        fun launchEdit(context:Context, bookItem: BookItem, books:List<Book> = emptyList()) {
            context.startActivity(
                Intent(context, RecordCreateActivity::class.java).apply {
                    putExtra(ActivityConst.KEY_BUNDLE_BOOKS, bundleOf(
                        ActivityConst.KEY_Type to ActivityConst.Const_Edit,
                        ActivityConst.KEY_Record to bookItem,
                        ActivityConst.KEY_BOOKS to books
                    ))
                }
            )
        }
    }
}