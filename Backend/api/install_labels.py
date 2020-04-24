#!/usr/bin/env python
from __future__ import print_function
from dotenv import load_dotenv, find_dotenv
from os import path, environ
import sys
from importlib import import_module
from neomodel import db, install_all_labels, remove_all_labels


def remove():
    bolt_url = environ.get("DB_URL")

    # Connect after to override any code in the module that may set the connection
    print('Connecting to {}\n'.format(bolt_url))
    db.set_connection(bolt_url)

    remove_all_labels()


def load_python_module_or_file(name):
    # Is a file
    if name.lower().endswith('.py'):
        basedir = path.dirname(path.abspath(name))
        # Add base directory to pythonpath
        sys.path.append(basedir)
        module_name = path.basename(name)[:-3]

    else:  # A module
        # Add current directory to pythonpath
        sys.path.append(path.abspath(path.curdir))

        module_name = name

    if module_name.startswith('.'):
        pkg = module_name.split('.')[1]
    else:
        pkg = None

    import_module(module_name, package=pkg)
    print("Loaded {}.".format(name))


def install():
    bolt_url = environ.get("DB_URL")

    load_python_module_or_file("main.py")

    # Connect after to override any code in the module that may set the connection
    print('Connecting to {}\n'.format(bolt_url))
    db.set_connection(bolt_url)

    install_all_labels()


if __name__ == '__main__':
    load_dotenv(find_dotenv())
    remove()
    install()
