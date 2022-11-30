echo -e '\033[32m'
echo '============================================================='
echo '$                                                           $'
echo '$                Nepxion Discovery Inspector                $'
echo '$                                                           $'
echo '$                                                           $'
echo '$                                                           $'
echo '$  Nepxion Studio All Right Reserved                        $'
echo '$  Copyright (C) 2017-2050                                  $'
echo '$                                                           $'
echo '============================================================='

echo -n $'\e'"]0;Nepxion Discovery Inspector"$'\a'

java -jar -Dfile.encoding=UTF-8 -Dnepxion.banner.shown.ansi.mode=true discovery-automation-inspector-1.1.0.jar --spring.config.location=./

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