package tv.superawesome.lib.saadloader;

import android.content.Context;

import java.util.List;
import java.util.Random;

import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.sanetwork.file.SAFileDownloader;
import tv.superawesome.lib.sanetwork.file.SAFileDownloaderInterface;

/**
 * Created by gabriel.coman on 22/09/16.
 */
public class SAGameWallParser {

    private Context context = null;

    public SAGameWallParser (Context context) {
        this.context = context;
    }

    public void getGameWallResources (List<SAAd> ads, SAGameWallParserInterface listener) {
        int max = ads.size();
        int first = 0;
        getImages(context, first, max, ads, listener);
    }

    private void getImages (final Context c, final int i, final int max, final List<SAAd> ads, final  SAGameWallParserInterface listener) {
        if (i > max - 1) {
            if (listener != null) {
                listener.gotAllImages();
            }
        } else {
            final SAAd ad = ads.get(i);
            ad.creative.details.media.playableMediaUrl = ad.creative.details.image;

            // create the image
            String[] names = ad.creative.details.media.playableMediaUrl.split("\\.");
            String ext = "png";
            if (names.length > 0) ext = names[names.length - 1];

            SAFileDownloader.getInstance().downloadFileFrom(c, ad.creative.details.media.playableMediaUrl, ext, new SAFileDownloaderInterface() {
                @Override
                public void response(boolean success, String diskUrl) {
                    ad.creative.details.media.isOnDisk = success;
                    ad.creative.details.media.playableDiskUrl = diskUrl;
                    getImages(c, i+1, max, ads, listener);
                }
            });
        }
    }
}
