package tv.superawesome.lib.saadloader;

import android.content.Context;

import java.util.List;

import tv.superawesome.lib.samodelspace.SAAd;
import tv.superawesome.lib.sanetwork.file.SAFileDownloader;
import tv.superawesome.lib.sanetwork.file.SAFileDownloaderInterface;

/**
 * Created by gabriel.coman on 22/09/16.
 */
public class SAAppWallParser {

    private Context context = null;

    public SAAppWallParser(Context context) {
        this.context = context;
    }

    public void getAppWallResources(List<SAAd> ads, SAAppWallParserInterface listener) {
        int max = ads.size();
        int first = 0;
        getImages(context, first, max, ads, listener);
    }

    private void getImages (final Context c, final int i, final int max, final List<SAAd> ads, final SAAppWallParserInterface listener) {
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

            SAFileDownloader.getInstance().downloadFileFrom(c, ad.creative.details.media.playableMediaUrl, new SAFileDownloaderInterface() {
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
