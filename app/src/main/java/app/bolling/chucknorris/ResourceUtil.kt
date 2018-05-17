package app.bolling.chucknorris

import android.app.Application
import android.graphics.drawable.Drawable
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Bolling on 18/10/16.
 */
@Singleton
class ResourceUtil @Inject
constructor(private val app: Application) {

    val country: String
        get() = app.resources.configuration.locale.country

    fun getString(id: Int): String {
        return app.resources.getString(id)
    }

    fun getDrawable(id: Int): Drawable? {
        return AppCompatResources.getDrawable(app, id)
    }

    fun getColor(color: Int): Int {
        return ContextCompat.getColor(app, color)
    }

    fun getStringColor(color: Int): String {
        return app.resources.getString(color)
    }

    fun getDimension(dimen: Int): Float {
        return app.resources.getDimension(dimen)
    }

    fun createVectorDrawable(icon_vehicle_blue: Int): Drawable? {
        return VectorDrawableCompat.create(app.resources, icon_vehicle_blue, null)
    }
}
