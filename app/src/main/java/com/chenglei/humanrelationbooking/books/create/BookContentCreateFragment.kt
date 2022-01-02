package com.chenglei.humanrelationbooking.books.create

import android.app.DatePickerDialog
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.core.widget.addTextChangedListener
import com.chenglei.humanrelationbooking.*
import com.chenglei.humanrelationbooking.base.launchBottomSheet
import com.chenglei.humanrelationbooking.databinding.FragmentBooksCreateBinding
import com.chenglei.humanrelationbooking.meta.Book
import com.chenglei.humanrelationbooking.meta.BookItem
import com.chenglei.humanrelationbooking.utils.toast
import com.chenglei.humanrelationbooking.vms.RelationItem
import com.hangshun.huadian.android.common.arch.FragmentBase
import com.hangshun.huadian.android.common.drawable.configDrawable
import com.hangshun.huadian.android.common.utils.dp2px
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

class BookContentCreateFragment : FragmentBase() {

    @InternalCoroutinesApi
    private val createVM by lazy {
        requireActivity().requireViewModel<CreateViewModel>()
    }


    override fun layoutId(): Int {
        return R.layout.fragment_books_create
    }

    @InternalCoroutinesApi
    override fun initViews(root: View) {
        arguments?.let { arguments->
            (arguments.getSerializable(ActivityConst.Key_Current_Book) as? Book)?.let { currentBook->
                createVM.chooseBook(currentBook)
            }
            (arguments.getSerializable(ActivityConst.Key_Current_Relation) as? RelationItem)?.let { relationItem ->
                createVM.updateRelationItem(relationItem)
            }
            (arguments.getSerializable(ActivityConst.KEY_Record) as? BookItem)?.let { bookItem ->
                createVM.initData(bookItem)
                (binding as? FragmentBooksCreateBinding)?.let { binding->
                    binding.username = bookItem.username
                }
            }
        }
        (binding as? FragmentBooksCreateBinding)?.let { binding ->
            binding.lifecycleOwner = requireActivity()
            binding.createVM = createVM
            binding.clicker = View.OnClickListener {
                when (it.id) {
                    R.id.tvSpend -> {
                        createVM.switchType(BookItemType.Spend)
                    }
                    R.id.tvIncome -> {
                        createVM.switchType(BookItemType.Income)
                    }
                    R.id.tvChooseBook -> {
                        // 选择账本
                        requireActivity().launchBottomSheet(BookListFragment::class.java, arguments)
                    }
                    R.id.tvRelation -> {
                        // 选择关系
                        requireActivity().launchBottomSheet(RelationSelectFragment::class.java)
                    }
                    R.id.tvTime -> {
                        // 选择时间
                        val ca = Calendar.getInstance()
                        DatePickerDialog(
                            requireContext(),
                            { view, year, month, dayOfMonth ->
                                createVM.updateTime(year, month, dayOfMonth)
                            },
                            ca.get(Calendar.YEAR),
                            ca.get(Calendar.MONTH),
                            ca.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                    R.id.btnSave -> {
                        //保存
                        val type = arguments?.getInt(ActivityConst.KEY_Type)?:ActivityConst.Const_Create
                        if (type == ActivityConst.Const_Create) {
                            createVM.saveRecord()
                        } else {
                            // 编辑
                                val record = arguments?.getSerializable(ActivityConst.KEY_Record) as? BookItem
                            record?.let {
                                createVM.editRecord(it)
                            }
                        }
                    }

                    R.id.chooseFromDirectory -> {
                        // 从联系人选择
                        this.requireActivity().launchDialog(RelationItemSelectFragment::class.java)
                    }
                }
            }

            listOf(500, 800, 1000, 1200).mapIndexed { index, money ->
                TextView(context).apply {
                    gravity = Gravity.CENTER
                    text = money.toString()
                    textSize = 16f
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.color_999999))
                    background = presetNoSelect()
                    setOnClickListener {
                        binding.presetContainer.forEachIndexed { clickIndex, view ->
                            view.background =
                                if (clickIndex == index) presetSelect() else presetNoSelect()
                            (view as? TextView)?.apply {
                                setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(), if (clickIndex == index) {
                                            R.color.primary_500
                                        } else R.color.color_999999
                                    )
                                )
                            }
                            createVM.inputMoney(money)
                        }
                    }
                }
            }.forEachIndexed { index, textView ->
                binding.presetContainer.addView(textView, LinearLayout.LayoutParams(
                    0, dp2px(requireContext(), 42f)
                ).apply {
                    weight = 1f
                    marginStart = if (index == 0) 0 else dp2px(requireContext(), 12f)
                })
            }

            binding.tvChooseUser.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                createVM.inputName(text.toString())
            })

            binding.etMoney.addTextChangedListener(onTextChanged = {text,_,_,_->
                val money = try {
                    text.toString().toInt()
                } catch (e:Exception){
                    e.printStackTrace()
                    0
                }

                createVM.inputMoney(money)
            })

            flowOb(createVM.submitSuccess) {
                it.takeIf { it }?.let {
                    toast(R.string.toast_save_success)
                    requireActivity().finish()
                }
            }

            flowOb(createVM.username) {
                binding.username = it
            }
        }
    }

    override fun dataBinding(): Boolean {
        return true
    }

    private fun presetNoSelect() = configDrawable(requireContext()) {
        radius = 4f
        stroke = 1f
        strokeColor = ContextCompat.getColor(requireContext(), R.color.color_e1e1e1)
    }

    private fun presetSelect() = configDrawable(requireContext()) {
        radius = 4f
        stroke = 1f
        strokeColor = ContextCompat.getColor(requireContext(), R.color.primary_500)
    }
}

enum class BookItemType(val type:Int) {
    Spend(2), // 给出
    Income (1)// 收到
}