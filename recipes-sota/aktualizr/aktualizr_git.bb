SUMMARY = "Aktualizr SOTA Client"
DESCRIPTION = "SOTA Client application written in C++"
HOMEPAGE = "https://github.com/advancedtelematic/aktualizr"
SECTION = "base"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=9741c346eef56131163e13b9db1241b3"
DEPENDS = "boost curl openssl jansson libsodium ostree"
RDEPENDS_${PN} = "python-petname"

SRC_URI = " \
  git://github.com/advancedtelematic/aktualizr \
  file://aktualizr-manual-provision.service \
  file://aktualizr-autoprovision.service \
  file://serial_command_petname.sh \
  file://sota_autoprov.toml \
  "
SRCREV = "ec82f19a50560018fb6b1940e07bffeaca21f5b3"
PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"
SYSTEMD_SERVICE_${PN} = "aktualizr.service"

inherit cmake systemd

EXTRA_OECMAKE = "-DWARNING_AS_ERROR=OFF -DCMAKE_BUILD_TYPE=Release -DBUILD_TESTS=OFF -DBUILD_OSTREE=ON"

export SOTA_AUTOPROVISION_CREDENTIALS

do_install_append() {
    if [ -n "${SOTA_AUTOPROVISION_CREDENTIALS}" -o -n "${SOTA_PACKED_CREDENTIALS}" ]; then
      install -d ${D}/${systemd_unitdir}/system
      install -m 0644 ${WORKDIR}/aktualizr-autoprovision.service ${D}/${systemd_unitdir}/system/aktualizr.service
      install -m 0755 ${WORKDIR}/serial_command_petname.sh ${D}/${bindir}/get_serial
      install -d ${D}/var/sota
      install -m "0644" ${WORKDIR}/sota_autoprov.toml ${D}/var/sota/sota.toml
    else
      install -d ${D}/${systemd_unitdir}/system
      install -m 0644 ${WORKDIR}/aktualizr-manual-provision.service ${D}/${systemd_unitdir}/system/aktualizr.service
    fi
}

FILES_${PN} = " \
                ${bindir}/aktualizr \
                ${bindir}/get_serial \
                ${systemd_unitdir}/system/aktualizr.service \
		/var/sota/sota.toml \
                "
