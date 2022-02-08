package app.khodko.catfood

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule


private const val GLIDE_MEMORY_CACHE = 10L * 1024 * 1024
private const val GLIDE_DISK_CACHE = 100L * 1024 * 1024

@GlideModule
class CatfoodGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemoryCache(LruResourceCache(GLIDE_MEMORY_CACHE))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, GLIDE_DISK_CACHE))

        /*
        // change the name of the folder the cache is placed in within external or internal storage:
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, "cacheFolderName", diskCacheSizeBytes))

        //calculate Bitmap pool
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(3)
            .build();
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));

        // size Bitmap pool
        int bitmapPoolSizeBytes = 1024 * 1024 * 30; // 30mb
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));
         */
    }

}