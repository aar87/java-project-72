start:
	make -C app start

build:
	make -C app build

lint:
	make -C app lint

test:
	make -C app test

report:
	make -C app report

clean:
	make -C app clean

install:
	make -C app clean install

.PHONY: build
