echo -e '\033[32m'
echo '============================================================='
echo '$                                                           $'
echo '$                 Nepxion Discovery Console                 $'
echo '$                                                           $'
echo '$                                                           $'
echo '$                                                           $'
echo '$  Nepxion Studio All Right Reserved                        $'
echo '$  Copyright (C) 2017-2050                                  $'
echo '$                                                           $'
echo '============================================================='

echo -n $'\e'"]0;Nepxion Discovery Console"$'\a'

java -jar -Dfile.encoding=UTF-8 -Dnepxion.banner.shown.ansi.mode=true discovery-console.jar --spring.cloud.nacos.discovery.server-addr=localhost:8848 --spring.cloud.nacos.config.server-addr=localhost:8848

function pause(){
  echo 'Press any key to continue...'
  read -n 1 -p "$*" str_inp
  if [ -z "$str_inp" ];then
    str_inp=1
  fi
    if [ $str_inp != '' ];then
      echo -ne '\b \n'
    fi
}

pause