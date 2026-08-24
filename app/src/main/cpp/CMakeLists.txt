cmake_minimum_required(VERSION 3.22.1)

project("security")

add_library(
        security
        SHARED
        security.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        security
        ${log-lib}
)

target_compile_options(security PRIVATE
        "-fvisibility=hidden"
)

target_link_options(security PRIVATE
        "-Wl,--no-rosegment"
        "-Wl,-z,max-page-size=16384"
        "-Wl,-z,common-page-size=16384"
)