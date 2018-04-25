package app.bolling.chucknorris;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Bolling on 18/10/16.
 */
@Singleton
public class ResourceUtil{

    private Application app;

    @Inject
    public ResourceUtil(Application app) {
        this.app = app;
    }

    public String getString(int id) {
        return app.getResources().getString(id);
    }

    public String getCountry() {
        return app.getResources().getConfiguration().locale.getCountry();
    }

    public Drawable getDrawable(int id) {
        return AppCompatResources.getDrawable(app, id);
    }

    public int getColor(int color) {
        return ContextCompat.getColor(app, color);
    }

    public String getStringColor(int color) {
        return app.getResources().getString(color);
    }

    public float getDimension(int dimen) {
        return app.getResources().getDimension(dimen);
    }

    public Drawable createVectorDrawable(int icon_vehicle_blue) {
        return VectorDrawableCompat.create(app.getResources(), icon_vehicle_blue, null);
    }
}
