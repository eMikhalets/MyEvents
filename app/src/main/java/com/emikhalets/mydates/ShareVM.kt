package com.emikhalets.mydates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emikhalets.mydates.utils.BottomBtnIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareVM @Inject constructor() : ViewModel() {

    private var bottomBtnClickFlag = false

    private val _bottomBtnClick = MutableLiveData<Boolean>()
    val bottomBtnClick get(): LiveData<Boolean> = _bottomBtnClick

    private val _bottomBtnIcon = MutableLiveData<BottomBtnIcon>()
    val bottomBtnIcon get(): LiveData<BottomBtnIcon> = _bottomBtnIcon

    fun setBottomBtnIcon(icon: BottomBtnIcon) {
        _bottomBtnIcon.value = icon
    }

    fun onClickBottomBtn() {
        bottomBtnClickFlag = !bottomBtnClickFlag
        _bottomBtnClick.value = bottomBtnClickFlag
    }
}