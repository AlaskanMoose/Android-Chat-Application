package tcss450.uw.edu.phishapp.SetLists;

import java.io.Serializable;

public class SetList implements Serializable {

    private final String mLongDate;
    private final String mLocation;
    private final String mVenue;
    private final String mSetListData;
    private final String mSetListNotes;
    private final String mUrl;


    public static class Builder {
        private final String mLongDate;
        private final String mLocation;
        private  String mVenue = "";
        private  String mSetListData = "";
        private  String mSetListNotes = "";
        private  String mUrl = "";


        /**
         *
         * @param longDate
         * @param location
         */
        public Builder(String longDate, String location) {
            this.mLongDate = longDate;
            this.mLocation = location;
        }


        public Builder addUrl(final String val) {
            mUrl = val;
            return this;
        }

        /**
         * Add an optional teaser for the full blog post.
         * @param venue an optional url teaser for the full blog post.
         * @return the Builder of this BlogPost
         */
        public Builder addVenue(final String venue) {
            mVenue = venue;
            return this;
        }

        /**
         * Add an optional author of the blog post.
         * @param val an optional author of the blog post.
         * @return the Builder of this BlogPost
         */
        public Builder addSetListData(final String val) {
            mSetListData = val;
            return this;
        }

        public Builder addSetListNotes(final String val) {
            mSetListNotes = val;
            return this;
        }

        public SetList build() {
            return new SetList(this);
        }

    }

    private SetList(final Builder builder) {
        this.mLongDate = builder.mLongDate;
        this.mLocation = builder.mLocation;
        this.mVenue = builder.mVenue;
        this.mSetListData = builder.mSetListData;
        this.mSetListNotes = builder.mSetListNotes;
        this.mUrl = builder.mUrl;
    }

    public String getLongDate() { return mLongDate;}

    public String getLocation() {
        return mLocation;
    }

    public String getVenue() {
        return mVenue;
    }

    public String getSetListData() {
        return mSetListData;
    }

    public String getSetListNotes() {
        return mSetListNotes;
    }

    public String getUrl(){ return mUrl;}
}
