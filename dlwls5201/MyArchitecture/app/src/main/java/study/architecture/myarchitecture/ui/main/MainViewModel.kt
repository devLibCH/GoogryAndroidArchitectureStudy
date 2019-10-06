package study.architecture.myarchitecture.ui.main

import androidx.databinding.ObservableField
import study.architecture.myarchitecture.base.BaseViewModel
import study.architecture.myarchitecture.data.repository.UpbitRepository
import study.architecture.myarchitecture.util.Filter
import timber.log.Timber

class MainViewModel(
    private val upbitRepository: UpbitRepository
) : BaseViewModel() {

    val titles = ObservableField<Array<String>>()
    val markets = ObservableField<Array<String>>()

    val pageLimit = ObservableField<Int>()

    val category = ObservableField<Filter.SelectArrow>()
    val isSelected = ObservableField<Boolean>(false)

    val selectField = ObservableField<Pair<Filter.SelectArrow, Boolean>>()

    fun loadData() {
        addDisposable(
            upbitRepository.getGroupedMarkets()
                .subscribe({ groupMarket ->

                    val keys = groupMarket.keys

                    titles.set(keys.toTypedArray())
                    pageLimit.set(keys.size)

                    val arrMarkets = Array(keys.size) { "" }

                    for ((index, value) in keys.withIndex()) {

                        arrMarkets[index] = groupMarket
                            .getValue(value)
                            .joinToString(separator = ",") { it.market }
                    }

                    markets.set(arrMarkets)

                }) {
                    Timber.e(it)
                }
        )
    }

    fun showCategoryArrow(selectArrow: Filter.SelectArrow) {

        this.category.set(selectArrow)

        val selected = !isSelected.get()!!
        this.isSelected.set(selected)

        selectField.set(Pair(selectArrow, selected))

    }
}