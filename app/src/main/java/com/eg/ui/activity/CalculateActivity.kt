package com.eg.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import com.eg.app.base.BaseActivity
import com.eg.app.util.OperationalUtils
import com.eg.databinding.ActivityCalculateBinding
import com.eg.viewmodel.state.EmptyViewModel
import me.hgj.jetpackmvvm.ext.view.isEmpty
import kotlin.math.max

@SuppressLint("SetTextI18n")
class CalculateActivity : BaseActivity<EmptyViewModel, ActivityCalculateBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }

    private lateinit var tvEnter: TextView
    private lateinit var tvResult: TextView
    private lateinit var tvHistory: TextView

    override fun initListener() {
        super.initListener()
        tvEnter = mDatabind.tvEnter
        tvResult = mDatabind.tvResult
        tvHistory = mDatabind.tvHistory

        mDatabind.apply {
            setOnClickListener(tv0, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvC, tvDel, tvDiv, tvTimes, tvMinus, tvPlus, tvEq, tvRem, tvPoint) {
                when (this) {
                    tv0 -> input("0", isDigital = true)
//                    tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9 -> inputDigital("1")
                    tv1 -> input("1", isDigital = true)
                    tv2 -> input("2", isDigital = true)
                    tv3 -> input("3", isDigital = true)
                    tv4 -> input("4", isDigital = true)
                    tv5 -> input("5", isDigital = true)
                    tv6 -> input("6", isDigital = true)
                    tv7 -> input("7", isDigital = true)
                    tv8 -> input("8", isDigital = true)
                    tv9 -> input("9", isDigital = true)
                    //清空
                    tvC -> input("C", isSymbol = true)
                    //回退
                    tvDel -> input("←", isSymbol = true)
                    //加
                    tvPlus -> input("＋", isSymbol = true)
                    //减
                    tvMinus -> input("－", isSymbol = true)
                    //乘
                    tvTimes -> input("×", isSymbol = true)
                    //除
                    tvDiv -> input("÷", isSymbol = true)
                    //百分百
                    tvRem -> {}
                    //点
                    tvPoint -> input(".")
                    //等于
                    tvEq -> {}
                }
            }

        }
    }

    private val enterValue get() = mDatabind.tvEnter.text.toString()
    private val enterEndElement get() = mDatabind.tvEnter.text.toString()

    private fun enterEndElement(): String {
        return if (tvEnter.isEmpty()) {
            ""
        } else {
            enterValue.substring(enterValue.length - 1)

        }
    }


    private fun input(s: String, isDigital: Boolean = false, isSymbol: Boolean = false) {
        val endElement = endElement()

        if (s == "C") {
            tvEnter.text = ""
            tvResult.text = ""
            return
        } else if (s == "←") {
            if (enterValue.length <= 1) {
                tvEnter.text = ""
                tvResult.text = "0"
            } else {
                tvEnter.text = enterValue.substring(0, enterValue.length - 1)
                if (checkContainsSymbol()) {
                    calculationResults()
                } else {
                    tvResult.text = enterValue
                }
            }
            return
        } else if (s == ".") {
            if (endElement.contains(".")) {
                return
            }
        } else if (isDigital) {
            if (endElement.contains(".")) {
                if (endElement.split(".")[1].length == 2) {
                    return
                }
            }
        }
        if (checkContainsSymbol()) {
            if (isSymbol) {
                if (endIndexIsSymbol()) {
                    tvEnter.text = enterValue.substring(0, enterValue.length - 1) + s
                } else {
                    tvEnter.text = enterValue + s
                }
            } else {
                tvEnter.text = enterValue + s
                calculationResults()
            }
        } else {
            tvEnter.text = enterValue + s
            if (isSymbol) {
                tvResult.text = enterValue.substring(0, enterValue.length - 1)
            } else {
                tvResult.text = enterValue
            }
        }

    }

    // 2+34+3-3+ 3*3÷2-2 +4÷2+ 1-6
    private fun calculationResults() {
        val list = arrayListOf<String>()
        var testValue = "0＋$enterValue" //这里运算式假设始终有 ＋
        if (endIndexIsSymbol()) {
            testValue = testValue.substring(0, testValue.length - 1)
        }
        val plusArr = testValue.split("＋")
        for (plusE in plusArr) {
            if (plusE.contains("－")) {
                val minusArr = plusE.split("－")
                for ((minusI, minusE) in minusArr.withIndex()) {
                    if (minusE.contains("×")) {
                        val list2 = arrayListOf<String>()
                        val timesE = minusE.split("×")
                        for (i in timesE) {
                            if (i.contains("÷")) {
                                val divE = i.split("÷")
                                for ((i2, e) in divE.withIndex()) {
                                    list2.add(if (i2 == 0) e else "÷$e")
                                }
                            } else {
                                list2.add(i)
                            }
                        }
                        if (list2.size != 0) {
                            list.add(if (minusI == 0) OperationalUtils.element2(list2) else "－${OperationalUtils.element2(list2)}")
                        }
                    } else if (minusE.contains("÷")) {
                        val list3 = arrayListOf<String>()
                        val divE = minusE.split("÷")
                        for ((i, e) in divE.withIndex()) {
                            list3.add(if (i == 0) e else "÷$e")
                        }
                        if (list3.size != 0) {
                            list.add(if (minusI == 0) OperationalUtils.element2(list3) else "－${OperationalUtils.element2(list3)}")
                        }
                    } else {
                        list.add(if (minusI == 0) minusE else "－$minusE")
                    }
                }
            } else if (plusE.contains("×")) {
                val list2 = arrayListOf<String>()
                val timesE = plusE.split("×")
                for (i in timesE) {
                    if (i.contains("÷")) {
                        val divE = i.split("÷")
                        for ((i2, e) in divE.withIndex()) {
                            list2.add(if (i2 == 0) e else "÷$e")
                        }
                    } else {
                        list2.add(i)
                    }
                }
                if (list2.size != 0) {
                    list.add(OperationalUtils.element2(list2))
                }
            } else if (plusE.contains("÷")) {
                val list3 = arrayListOf<String>()
                val divE = plusE.split("÷")
                for ((i, e) in divE.withIndex()) {
                    list3.add(if (i == 0) e else "÷$e")
                }
                if (list3.size != 0) {
                    list.add(OperationalUtils.element2(list3))
                }
            } else {
                list.add(plusE)
            }


        }
        val element = OperationalUtils.element(list)
        tvResult.text = element

    }

    private fun calculationResults1() {
        if (enterValue.contains("＋")) {
            val split = enterValue.split("＋")
            val params1 = split[0].toDouble()
            val params2 = split[1].toDouble()
            tvResult.text = params1.plus(params2).toString()
        } else if (enterValue.contains("－")) {
            val split = enterValue.split("－")
            val params1 = split[0].toDouble()
            val params2 = split[1].toDouble()
            tvResult.text = params1.minus(params2).toString()
        } else if (enterValue.contains("×")) {
            val split = enterValue.split("×")
            val params1 = split[0].toDouble()
            val params2 = split[1].toDouble()
            tvResult.text = params1.times(params2).toString()
        } else if (enterValue.contains("÷")) {
            val split = enterValue.split("÷")
            val params1 = split[0].toDouble()
            val params2 = split[1].toDouble()
            tvResult.text = params1.div(params2).toString()
        }
    }

    /**
     * 最后一个元素是 ＋－×÷
     */
    private fun endIndexIsSymbol(): Boolean {
        if (tvEnter.isEmpty()) {
            return false
        } else {
            if ("＋－×÷".contains(enterValue.substring(enterValue.length - 1))) {
                return true
            }
            return false
        }
    }

    /**
     * 计算区是否是包含 ＋－×÷
     */
    private fun checkContainsSymbol(): Boolean {
        return (enterValue.contains("＋") || enterValue.contains("－") || enterValue.contains("×") || enterValue.contains("÷"))
    }

    private fun endElement(): String {
        var index1 = 0
        var index2 = 0
        var index3 = 0
        var index4 = 0
        if (enterValue.contains("＋")) {
            index1 = enterValue.lastIndexOf("＋")
        }
        if (enterValue.contains("－")) {
            index2 = enterValue.lastIndexOf("－")
        }
        if (enterValue.contains("×")) {
            index3 = enterValue.lastIndexOf("×")
        }
        if (enterValue.contains("÷")) {
            index4 = enterValue.lastIndexOf("÷")
        }
        val maxIndex = max(max(index1, index2), max(index3, index4))

        if (maxIndex == 0) {
            println("1234---" + enterValue)
            return enterValue
        } else {
            println("1234---" + enterValue.substring(maxIndex + 1))
            return enterValue.substring(maxIndex + 1)
        }
    }

}