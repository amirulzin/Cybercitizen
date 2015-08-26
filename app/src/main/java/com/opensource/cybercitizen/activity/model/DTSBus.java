package com.opensource.cybercitizen.activity.model;


import android.location.Location;

public class DTSBus
{
    private String station;
    private String locationHint;
    private double latitude;
    private double longitude;

    public DTSBus()
    {
    }

    public DTSBus(final String station, final String locationHint, final double latitude, final double longitude)
    {
        this.station = station;
        this.locationHint = locationHint;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString()
    {
        return "DTSBus{" +
                "station='" + station + '\'' +
                ", locationHint='" + locationHint + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String getStation()
    {
        return station;
    }

    public String getLocationHint()
    {
        return locationHint;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public Location getLocation()
    {
        Location out = new Location(station);
        out.setLatitude(latitude);
        out.setLongitude(longitude);
        return out;
    }

    public float getDistanceTo(Location location)
    {
        return getLocation().distanceTo(location);
    }

    public static class Dataset
    {
        private static final String[] streetName = "CYBERJAYA LAKE CARDEN,CYBERJAYA LAKE CARDEN,D'PULZE CYBERJAYA,D'PULZE CYBERJAYA,CYBER 5,CYBER 5,GLOMAC CYBERJAYA (BARAT),GLOMAC CYBERJAYA (BARAT),WISMA SHELL,WISMA SHELL,QUILL 4 CYBERJAYA,QUILL 4 CYBERJAYA,GLOMAC CYBERJAYA (TIMUR),GLOMAC CYBERJAYA (TIMUR),LIM KOK WING UNIVERSITY (TIMUR),LIM KOK WING UNIVERSITY (TIMUR),LIM KOK WING UNIVERSITY (BARAT),LIM KOK WING UNIVERSITY (BARAT),ZON TEKNOLOGI MKN EMBASSY,ZON TEKNOLOGI MKN EMBASSY,MULTIMEDIA DEVELOPMENT CORPORATION (MDEC),MULTIMEDIA DEVELOPMENT CORPORATION (MDEC),CENTURY SQUARE,CENTURY SQUARE,SHAFTSBURY SQUARE,SHAFTSBURY SQUARE,CYBERVIEW GARDEN VILLAS,CYBERVIEW GARDEN VILLAS,PERDANA LAKEVIEW EAST,PERDANA LAKEVIEW EAST,PERDANA LAKEVIEW WEST,PERDANA LAKEVIEW WEST,SEK SERI PUTERI CYBERJAYA,SEK SERI PUTERI CYBERJAYA,CYBER SPORTS ARENA,CYBER SPORTS ARENA,PRIMA AVENUE 1,PRIMA AVENUE 1,UNIVERSITI MALAYSIA OF COMPUTER SCIENCE AND ENGINEERING,UNIVERSITI MALAYSIA OF COMPUTER SCIENCE AND ENGINEERING,MAGIC CENTER,MAGIC CENTER,KOMPLEKS BALAI POLIS CYBER,KOMPLEKS BALAI POLIS CYBER,BALAI BOMBA CYBER,BALAI BOMBA CYBER,MENARA HASIL CYBER (UTARA),MENARA HASIL CYBER (UTARA),MENARA HASIL CYBER (BARAT),MENARA HASIL CYBER (BARAT),THE DOMAIN NEOCYBER (TIMUR),THE DOMAIN NEOCYBER (SELATAN),THE DOMAIN NEOCYBER (SELATAN),CYBERIA CRESCENT 2,CYBERIA CRESCENT 2,MIGHT CYBERJAYA,MIGHT CYBERJAYA,CYBERJAYA TRANSPORT TERMINAL,MMU CYBERJAYA,MMU CYBERJAYA,CYBERIA CRESCENT CONDOMINIUM,CYBERIA CRESCENT CONDOMINIUM,CYBERJAYA SMART SCHOOL COMPLEX,CYBERJAYA SMART SCHOOL COMPLEX,THE ARC CYBERJAYA ,THE ARC CYBERJAYA ,MMU CYBERJAYA ,NO_NAME,NO_NAME".split(",");

        private static final String[] locationHint = "Hadapan MMU,Bertentangan MMU,Hadapan D'Puzle Cyberjaya,Hadapan Cyberjaya Transport Terminal,Hadapan HSBC,Hadapan BMW Showroom,Hadapan Quill 18,Hadapan Glomac Cyberjaya,Hadapan Wisma Shell,Bertentangan Wisma Shell,Hadapan Innovation Centre,Hadapan DHL Cyber,Hadapan Glomac Cyberjaya,Bertentangan Glomac Cyberjaya,Hadapan Lim Kok Wing University,Bertentangan Lim Kok Wing University,Hadapan Lim Kok Wing University,Bertentangan Lim Kok Wing University,Bertentangan MKN Embassy Techzone,Hadapan MKN Embassy Techzone,Bertentangan MSC HQ,Hadapan MSC HQ,Bertentangan Shaftsbury Square,Hadapan Shaftsbury Square,Bertentangan Shaftsbury King Bakery,Hadapan King Bakery,Hadapan Multimedia Super Coridor,Hadapan Cyber View Garden,Hadapan Perdana Lake View East,Bertentangan Perdana Lake View East  ,Bertentangan Perdana Lake View West ,Hadapan Perdana Lake View West ,Hadapan Cyber Height Villa,Hadapan Sekolah Seri Puteri,Hadapan Cyber Sport Arena,Bertentangan Cyber Sport Arena,Hadapan Prima Avenue 1,Hadapan Prima Avenue 1,Bertentangan Wisma Avis ,Hadapan Axis Enreka ,Hadapan Wisma Dell,Hadapan Magic ,Hadapan Basic Bay,Hadapan Kompleks Balai Polis Cyber,Hadapan Balai Bomba Cyber,Hadapan Wisma Dell,Hadapan LHDN Cyber,Bertentangan LHDN Cyber,Hadapan LHDN Cyber,Hadapan Wisma HP,Hadapan The Domain Cyber,Hadapan Wisma Mustapha Kamal,Hadapan CSF Cmputer Data Centre ,Hadapan Cyberia Crescent 2,Hadapan D'Melur Condo,Bertentangan MIGHT,Hadapan MIGHT,Hentian Bas Cyberjaya,Hadapan Cyberjaya Crescent Kondo,Bertentangan Cyberia Crescent Kondo,Hadapan SK Cyberjaya 1,Bertentangan SK Cyberjaya 1,Hadapan SMK Cyberjaya ,Bertentangan SMK Cyberjaya ,Hadapan The ARC Cyberjaya,Hadapan Cyberia Kondo,Hadapan MMU,Hadapan PMU Air Hitam ,Hadapan Laman Bayu".split(",");

        private static final double latitude[] = new double[]{
                2.932927778, 2.933080556, 2.921561111, 2.921211111, 2.922233333, 2.922052778, 2.924322222, 2.925483333, 2.927141667, 2.927283333, 2.922836111, 2.922536111, 2.92525, 2.925083333, 2.941252778, 2.941713889, 2.939961111, 2.940086111, 2.936669444, 2.937769444, 2.923583333, 2.923347222, 2.923913889, 2.923675, 2.923705556, 2.923436111, 2.921416667, 2.920438889, 2.917825, 2.917625, 2.9151, 2.915241667, 2.909663889, 2.909886111, 2.92675, 2.926888889, 2.920936111, 2.920866667, 2.915613889, 2.916080556, 2.910422222, 2.90985, 2.906283333, 2.9064, 2.911988889, 2.911622222, 2.910988889, 2.911225, 2.909508333, 2.909677778, 2.917019444, 2.914694444, 2.914486111, 2.920588889, 2.920541667, 2.920802778, 2.920769444, 2.920497222, 2.922366667, 2.922563889, 2.919327778, 2.919422222, 2.919033333, 2.919130556, 2.925133333, 2.925283333, 2.922836111, 2.961313889, 2.960772222
        };

        private static final double longitude[] = new double[]{
                101.6427694, 101.6429583, 101.6512306, 101.6507694, 101.6539028, 101.6541667, 101.6553806, 101.6555444, 101.6546333, 101.6548194, 101.6564417, 101.6562222, 101.6572833, 101.6576611, 101.6634083, 101.6632611, 101.6603611, 101.6603528, 101.6558778, 101.6552722, 101.65945, 101.6595222, 101.6612583, 101.6613306, 101.6634111, 101.6634778, 101.6665806, 101.6668806, 101.6683472, 101.6681806, 101.6691222, 101.66885, 101.6652639, 101.6653083, 101.6616111, 101.6616722, 101.65865, 101.658425, 101.6575611, 101.6574417, 101.6559639, 101.6558139, 101.6531889, 101.6534083, 101.6581028, 101.6580167, 101.6496222, 101.6496778, 101.6492417, 101.6489028, 101.6502222, 101.649225, 101.6493056, 101.6467361, 101.6469722, 101.6555861, 101.6554917, 101.6518389, 101.6408306, 101.6405694, 101.6378806, 101.6375667, 101.6399583, 101.6403, 101.6374944, 101.6377444, 101.642825, 101.6234083, 101.6235778
        };

        public static DTSBus getData(int index)
        {
            return new DTSBus(streetName[index], locationHint[index], latitude[index], longitude[index]);
        }

        public static int[] getLengths()
        {
            return new int[]{streetName.length, locationHint.length, latitude.length, longitude.length};
        }

        public static int getLength()
        {
            return streetName.length;
        }

        public static boolean isUnindentifiedStation(String stationName)
        {
            if (stationName.trim().equalsIgnoreCase("NO_NAME"))
            {
                return true;
            }
            return false;
        }

        public static DTSBus getReplacementForUnindentifiedStation()
        {
            return getData(4);
        }
    }
}
