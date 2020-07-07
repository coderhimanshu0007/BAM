package com.teamcomputers.bam.Models

import android.os.Parcel
import android.os.Parcelable

class Token() : Parcelable {
    /*val CREATOR: Parcelable.Creator<Token> = object : Parcelable.Creator<Token> {
        override fun createFromParcel(source: Parcel): Token {
            return Token(source)
        }

        override fun newArray(size: Int): Array<Token> {
            return arrayOfNulls(size)
        }
    }*/
    private var generatedToken: String? = null
    private var fireBaseToken: String? = null

    constructor(parcel: Parcel) : this() {
        generatedToken = parcel.readString()
        fireBaseToken = parcel.readString()
    }

    fun Token(generatedToken: String, fireBaseToken: String) {
        this.generatedToken = generatedToken
        this.fireBaseToken = fireBaseToken
    }

    protected fun Token(`in`: Parcel) {
        this.generatedToken = `in`.readString()
        this.fireBaseToken = `in`.readString()
    }

    fun getGeneratedToken(): String? {
        return generatedToken
    }

    fun setGeneratedToken(generatedToken: String) {
        this.generatedToken = generatedToken
    }

    fun getFireBaseToken(): String? {
        return fireBaseToken
    }

    fun setFireBaseToken(fireBaseToken: String) {
        this.fireBaseToken = fireBaseToken
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.generatedToken)
        dest.writeString(this.fireBaseToken)
    }

    companion object CREATOR : Parcelable.Creator<Token> {
        override fun createFromParcel(parcel: Parcel): Token {
            return Token(parcel)
        }

        override fun newArray(size: Int): Array<Token?> {
            return arrayOfNulls(size)
        }
    }
}