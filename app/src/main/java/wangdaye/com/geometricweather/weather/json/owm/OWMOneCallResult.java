package wangdaye.com.geometricweather.weather.json.owm;

import java.util.Date;

/**
 * Accu realtime result.
 * */

public class OWMOneCallResult {

    /**
     {
         "lat": 33.44,
         "lon": -94.04,
         "timezone": "America/Chicago",
         "timezone_offset": -18000,
         "current": {
             "dt": 1588935779,
             "sunrise": 1588936856,
             "sunset": 1588986260,
             "temp": 16.75,
             "feels_like": 16.07,
             "pressure": 1009,
             "humidity": 93,
             "dew_point": 15.61,
             "uvi": 8.97,
             "clouds": 90,
             "visibility": 12874,
             "wind_speed": 3.6,
             "wind_deg": 280,
             "weather": [
                 {
                 "id": 501,
                 "main": "Rain",
                 "description": "moderate rain",
                 "icon": "10n"
                 },
                 {
                 "id": 200,
                 "main": "Thunderstorm",
                 "description": "thunderstorm with light rain",
                 "icon": "11n"
                 }
             ],
             "rain": {
                "1h": 2.79
             }
         },
         "minutely": [
             {
             "dt": 1588935780,
             "precipitation": 2.789
             },
            ...
         },
         "hourly": [
             {
             "dt": 1588935600,
             "temp": 16.75,
             "feels_like": 13.93,
             "pressure": 1009,
             "humidity": 93,
             "dew_point": 15.61,
             "clouds": 90,
             "wind_speed": 6.66,
             "wind_deg": 203,
             "weather": [
             {
             "id": 501,
             "main": "Rain",
             "description": "moderate rain",
             "icon": "10n"
             }
             ],
             "rain": {
             "1h": 2.92
             }
             },
             ...
         }
         "daily": [
             {
             "dt": 1588960800,
             "sunrise": 1588936856,
             "sunset": 1588986260,
             "temp": {
             "day": 22.49,
             "min": 10.96,
             "max": 22.49,
             "night": 10.96,
             "eve": 18.45,
             "morn": 18.14
             },
             "feels_like": {
             "day": 18.72,
             "night": 6.53,
             "eve": 16.34,
             "morn": 16.82
             },
             "pressure": 1014,
             "humidity": 60,
             "dew_point": 14.35,
             "wind_speed": 7.36,
             "wind_deg": 342,
             "weather": [
                 {
                 "id": 502,
                 "main": "Rain",
                 "description": "heavy intensity rain",
                 "icon": "10d"
                 }
             ],
             "clouds": 68,
             "rain": 15.38,
             "uvi": 8.97
             },
             ...
     }
     */

    public double lat;
    public double lon;
    public String timezone;
    public int timezone_offset;

    public Current current;
    public Minutely minutely;
    public Hourly hourly;
    public Daily daily;


    public static class Current {
        public long dt;
        public long sunrise;
        public long sunset;
        public double temp;
        public double feels_like;
        public int pressure;
        public int humidity;
        public double dew_point;
        public double uvi;
        public int clouds;
        public int visibility;
        public double wind_speed;
        public double wind_gust;
        public int wind_deg;
        public Rain rain;
        public Snow snow;
        public Weather weather;

        public static class Rain {
            public double oneh;
        }
        public static class Snow {
            public double oneh;
        }
        public static class Weather {
            
        }
    }

    public static class Temperature {
        /**
         * Value : 3.9
         * Unit : C
         * UnitType : 17
         */

        public Metric Metric;
        /**
         * Value : 39.0
         * Unit : F
         * UnitType : 18
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class RealFeelTemperature {
        /**
         * Value : 1.4
         * Unit : C
         * UnitType : 17
         */

        public Metric Metric;
        /**
         * Value : 35.0
         * Unit : F
         * UnitType : 18
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class RealFeelTemperatureShade {
        /**
         * Value : -0.6
         * Unit : C
         * UnitType : 17
         */

        public Metric Metric;
        /**
         * Value : 31.0
         * Unit : F
         * UnitType : 18
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class DewPoint {
        /**
         * Value : -0.2
         * Unit : C
         * UnitType : 17
         */

        public Metric Metric;
        /**
         * Value : 32.0
         * Unit : F
         * UnitType : 18
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class Wind {
        /**
         * Degrees : 315
         * Localized : 西北
         * English : NW
         */

        public Direction Direction;
        /**
         * Metric : {"Value":16.7,"Unit":"km/h","UnitType":7}
         * Imperial : {"Value":10.4,"Unit":"mi/h","UnitType":9}
         */

        public Speed Speed;

        public static class Direction {
            public int Degrees;
            public String Localized;
            public String English;
        }

        public static class Speed {
            /**
             * Value : 16.7
             * Unit : km/h
             * UnitType : 7
             */

            public Metric Metric;
            /**
             * Value : 10.4
             * Unit : mi/h
             * UnitType : 9
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }
    }

    public static class WindGust {
        /**
         * Metric : {"Value":16.7,"Unit":"km/h","UnitType":7}
         * Imperial : {"Value":10.4,"Unit":"mi/h","UnitType":9}
         */

        public Speed Speed;

        public static class Speed {
            /**
             * Value : 16.7
             * Unit : km/h
             * UnitType : 7
             */

            public Metric Metric;
            /**
             * Value : 10.4
             * Unit : mi/h
             * UnitType : 9
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }
    }

    public static class Visibility {
        /**
         * Value : 4.8
         * Unit : km
         * UnitType : 6
         */

        public Metric Metric;
        /**
         * Value : 3.0
         * Unit : mi
         * UnitType : 2
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class Ceiling {
        /**
         * Value : 579.0
         * Unit : m
         * UnitType : 5
         */

        public Metric Metric;
        /**
         * Value : 1900.0
         * Unit : ft
         * UnitType : 0
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class Pressure {
        /**
         * Value : 1022.0
         * Unit : mb
         * UnitType : 14
         */

        public Metric Metric;
        /**
         * Value : 30.18
         * Unit : inHg
         * UnitType : 12
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class PressureTendency {
        public String LocalizedText;
        public String Code;
    }

    public static class Past24HourTemperatureDeparture {
        /**
         * Value : -1.1
         * Unit : C
         * UnitType : 17
         */

        public Metric Metric;
        /**
         * Value : -2.0
         * Unit : F
         * UnitType : 18
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class ApparentTemperature {
        /**
         * Value : 3.9
         * Unit : C
         * UnitType : 17
         */

        public Metric Metric;
        /**
         * Value : 39.0
         * Unit : F
         * UnitType : 18
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class WindChillTemperature {
        /**
         * Value : 0.0
         * Unit : C
         * UnitType : 17
         */

        public Metric Metric;
        /**
         * Value : 32.0
         * Unit : F
         * UnitType : 18
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class WetBulbTemperature {
        /**
         * Value : 2.4
         * Unit : C
         * UnitType : 17
         */

        public Metric Metric;
        /**
         * Value : 36.0
         * Unit : F
         * UnitType : 18
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class Precip1hr {
        /**
         * Value : 0.0
         * Unit : mm
         * UnitType : 3
         */

        public Metric Metric;
        /**
         * Value : 0.0
         * Unit : in
         * UnitType : 1
         */

        public Imperial Imperial;

        public static class Metric {
            public double Value;
            public String Unit;
            public int UnitType;
        }

        public static class Imperial {
            public double Value;
            public String Unit;
            public int UnitType;
        }
    }

    public static class PrecipitationSummary {
        /**
         * Metric : {"Value":0,"Unit":"mm","UnitType":3}
         * Imperial : {"Value":0,"Unit":"in","UnitType":1}
         */

        public Precipitation Precipitation;
        /**
         * Metric : {"Value":0,"Unit":"mm","UnitType":3}
         * Imperial : {"Value":0,"Unit":"in","UnitType":1}
         */

        public PastHour PastHour;
        /**
         * Metric : {"Value":0,"Unit":"mm","UnitType":3}
         * Imperial : {"Value":0,"Unit":"in","UnitType":1}
         */

        public Past3Hours Past3Hours;
        /**
         * Metric : {"Value":0,"Unit":"mm","UnitType":3}
         * Imperial : {"Value":0,"Unit":"in","UnitType":1}
         */

        public Past6Hours Past6Hours;
        /**
         * Metric : {"Value":0,"Unit":"mm","UnitType":3}
         * Imperial : {"Value":0,"Unit":"in","UnitType":1}
         */

        public Past9Hours Past9Hours;
        /**
         * Metric : {"Value":0,"Unit":"mm","UnitType":3}
         * Imperial : {"Value":0,"Unit":"in","UnitType":1}
         */

        public Past12Hours Past12Hours;
        /**
         * Metric : {"Value":0,"Unit":"mm","UnitType":3}
         * Imperial : {"Value":0,"Unit":"in","UnitType":1}
         */

        public Past18Hours Past18Hours;
        /**
         * Metric : {"Value":1,"Unit":"mm","UnitType":3}
         * Imperial : {"Value":0.05,"Unit":"in","UnitType":1}
         */

        public Past24Hours Past24Hours;

        public static class Precipitation {
            /**
             * Value : 0.0
             * Unit : mm
             * UnitType : 3
             */

            public Metric Metric;
            /**
             * Value : 0.0
             * Unit : in
             * UnitType : 1
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }

        public static class PastHour {
            /**
             * Value : 0.0
             * Unit : mm
             * UnitType : 3
             */

            public Metric Metric;
            /**
             * Value : 0.0
             * Unit : in
             * UnitType : 1
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }

        public static class Past3Hours {
            /**
             * Value : 0.0
             * Unit : mm
             * UnitType : 3
             */

            public Metric Metric;
            /**
             * Value : 0.0
             * Unit : in
             * UnitType : 1
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }

        public static class Past6Hours {
            /**
             * Value : 0.0
             * Unit : mm
             * UnitType : 3
             */

            public Metric Metric;
            /**
             * Value : 0.0
             * Unit : in
             * UnitType : 1
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }

        public static class Past9Hours {
            /**
             * Value : 0.0
             * Unit : mm
             * UnitType : 3
             */

            public Metric Metric;
            /**
             * Value : 0.0
             * Unit : in
             * UnitType : 1
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }

        public static class Past12Hours {
            /**
             * Value : 0.0
             * Unit : mm
             * UnitType : 3
             */

            public Metric Metric;
            /**
             * Value : 0.0
             * Unit : in
             * UnitType : 1
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }

        public static class Past18Hours {
            /**
             * Value : 0.0
             * Unit : mm
             * UnitType : 3
             */

            public Metric Metric;
            /**
             * Value : 0.0
             * Unit : in
             * UnitType : 1
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }

        public static class Past24Hours {
            /**
             * Value : 1.0
             * Unit : mm
             * UnitType : 3
             */

            public Metric Metric;
            /**
             * Value : 0.05
             * Unit : in
             * UnitType : 1
             */

            public Imperial Imperial;

            public static class Metric {
                public double Value;
                public String Unit;
                public int UnitType;
            }

            public static class Imperial {
                public double Value;
                public String Unit;
                public int UnitType;
            }
        }
    }

    public static class TemperatureSummary {
        /**
         * Minimum : {"Metric":{"Value":1,"Unit":"C","UnitType":17},"Imperial":{"Value":34,"Unit":"F","UnitType":18}}
         * Maximum : {"Metric":{"Value":4.1,"Unit":"C","UnitType":17},"Imperial":{"Value":39,"Unit":"F","UnitType":18}}
         */

        public Past6HourRange Past6HourRange;
        /**
         * Minimum : {"Metric":{"Value":1,"Unit":"C","UnitType":17},"Imperial":{"Value":34,"Unit":"F","UnitType":18}}
         * Maximum : {"Metric":{"Value":4.9,"Unit":"C","UnitType":17},"Imperial":{"Value":41,"Unit":"F","UnitType":18}}
         */

        public Past12HourRange Past12HourRange;
        /**
         * Minimum : {"Metric":{"Value":1,"Unit":"C","UnitType":17},"Imperial":{"Value":34,"Unit":"F","UnitType":18}}
         * Maximum : {"Metric":{"Value":5.4,"Unit":"C","UnitType":17},"Imperial":{"Value":42,"Unit":"F","UnitType":18}}
         */

        public Past24HourRange Past24HourRange;

        public static class Past6HourRange {
            /**
             * Metric : {"Value":1,"Unit":"C","UnitType":17}
             * Imperial : {"Value":34,"Unit":"F","UnitType":18}
             */

            public Minimum Minimum;
            /**
             * Metric : {"Value":4.1,"Unit":"C","UnitType":17}
             * Imperial : {"Value":39,"Unit":"F","UnitType":18}
             */

            public Maximum Maximum;

            public static class Minimum {
                /**
                 * Value : 1.0
                 * Unit : C
                 * UnitType : 17
                 */

                public Metric Metric;
                /**
                 * Value : 34.0
                 * Unit : F
                 * UnitType : 18
                 */

                public Imperial Imperial;

                public static class Metric {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }

                public static class Imperial {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }
            }

            public static class Maximum {
                /**
                 * Value : 4.1
                 * Unit : C
                 * UnitType : 17
                 */

                public Metric Metric;
                /**
                 * Value : 39.0
                 * Unit : F
                 * UnitType : 18
                 */

                public Imperial Imperial;

                public static class Metric {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }

                public static class Imperial {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }
            }
        }

        public static class Past12HourRange {
            /**
             * Metric : {"Value":1,"Unit":"C","UnitType":17}
             * Imperial : {"Value":34,"Unit":"F","UnitType":18}
             */

            public Minimum Minimum;
            /**
             * Metric : {"Value":4.9,"Unit":"C","UnitType":17}
             * Imperial : {"Value":41,"Unit":"F","UnitType":18}
             */

            public Maximum Maximum;

            public static class Minimum {
                /**
                 * Value : 1.0
                 * Unit : C
                 * UnitType : 17
                 */

                public Metric Metric;
                /**
                 * Value : 34.0
                 * Unit : F
                 * UnitType : 18
                 */

                public Imperial Imperial;

                public static class Metric {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }

                public static class Imperial {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }
            }

            public static class Maximum {
                /**
                 * Value : 4.9
                 * Unit : C
                 * UnitType : 17
                 */

                public Metric Metric;
                /**
                 * Value : 41.0
                 * Unit : F
                 * UnitType : 18
                 */

                public Imperial Imperial;

                public static class Metric {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }

                public static class Imperial {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }
            }
        }

        public static class Past24HourRange {
            /**
             * Metric : {"Value":1,"Unit":"C","UnitType":17}
             * Imperial : {"Value":34,"Unit":"F","UnitType":18}
             */

            public Minimum Minimum;
            /**
             * Metric : {"Value":5.4,"Unit":"C","UnitType":17}
             * Imperial : {"Value":42,"Unit":"F","UnitType":18}
             */

            public Maximum Maximum;

            public static class Minimum {
                /**
                 * Value : 1.0
                 * Unit : C
                 * UnitType : 17
                 */

                public Metric Metric;
                /**
                 * Value : 34.0
                 * Unit : F
                 * UnitType : 18
                 */

                public Imperial Imperial;

                public static class Metric {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }

                public static class Imperial {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }
            }

            public static class Maximum {
                /**
                 * Value : 5.4
                 * Unit : C
                 * UnitType : 17
                 */

                public Metric Metric;
                /**
                 * Value : 42.0
                 * Unit : F
                 * UnitType : 18
                 */

                public Imperial Imperial;

                public static class Metric {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }

                public static class Imperial {
                    public double Value;
                    public String Unit;
                    public int UnitType;
                }
            }
        }
    }
}
