DROP TABLE IF EXISTS "tb_parking_spot";
CREATE TABLE "public"."tb_parking_spot"
(
    "id"                  uuid                   NOT NULL,
    "apartment"           character varying(30)  NOT NULL,
    "block"               character varying(30)  NOT NULL,
    "brand_car"           character varying(70)  NOT NULL,
    "color_car"           character varying(70)  NOT NULL,
    "license_plate_car"   character varying(7)   NOT NULL,
    "model_car"           character varying(70)  NOT NULL,
    "parking_spot_number" character varying(10)  NOT NULL,
    "registration_date"   timestamp              NOT NULL,
    "responsible_name"    character varying(130) NOT NULL,
    CONSTRAINT "tb_parking_spot_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "uk_678owtycsgr3anxf3qw4s9r8u" UNIQUE ("parking_spot_number"),
    CONSTRAINT "uk_sms6qglh44hhw4bpgwnp8umw1" UNIQUE ("license_plate_car")
) WITH (oids = false);
