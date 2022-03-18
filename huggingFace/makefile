#############################################
#                                           #
# MAKEFILE FOR DEFINING A MANAGEMENT CLI    #
#                                           #
#############################################

# Setup the shell and python version.
# It's necessary to set this because some environments don't link sh -> bash.
SHELL := /bin/bash
PYTHON := python3

# Constants
VENV_DIR_LINK = venv
VENV_DIR = .hug-face-venv
TEST_VENV_DIR = .testing-venv

.PHONY: help clean clean-venv clean-temp-files


#################################
#                               #
# HELP                      	#
#                               #
#################################
help:
	@echo "usage: make <command>"
	@echo
	@echo "The available commands are:"
	@echo "  clean              [alias] for running \"clean-venv\" and \"clean-temp-files\" sequentially."
	@echo "  clean-venv         to clean the virtualenv directories and links."
	@echo "  clean-temp-files   to cleanup the project from temporary files."
	@echo "  venv-app           to create the applications virtualenv and its symbolic links."
	@echo "  venv-test          to create the testing virtualenv for hosting dev requirements and running tests."
	@echo "  install            to install the app, runs \"venv\" automatically."
	@echo "  install-dev        to install the app in dev mode, runs \"venv-test\" automatically."
	@echo "  pytest             to run the entire tests package."
	@echo "  help               to display this message."
	@echo
	@echo "All testing commands take a \"VERBOSITY\" argument which is by default set to 'vv'. It controls the verbosity of the pytest command. For example: \`make unit-tests VERBOSITY=qq\`."

#################################
#                               #
# CLEAN-UP                  	#
#                               #
#################################
clean-venv:
	@echo "####################################################"
	@echo "# CLEANING UP THE VIRTUAL ENVIRONMENT              #"
	@echo "####################################################"
	@rm -rf $(VENV_DIR)
	@rm -rf $(VENV_DIR_LINK)
	@rm -rf $(TEST_VENV_DIR)
	@echo "Done!"

clean-temp-files:
	@echo "####################################################"
	@echo "# CLEANING UP CACHED AND TMP FILES                 #"
	@echo "####################################################"
	@rm -rf build
	@rm -rf dist
	@rm -rf *.egg-info
	@rm -rf .tox
	@rm -rf htmlcov
	@rm -rf .cache
	@rm -rf .pytest_cache
	@find . -name "*.*,cover" -delete
	@find . -name "*.pyc" -delete
	@find . -name "*.pyo" -delete
	@find . -name "__pycache__" -exec rm -rf {} \; || echo "";
	@echo "Done!"

clean: clean-venv clean-temp-files


#################################
#                               #
# VIRTUAL ENVIRONMENT       	#
#                               #
#################################
log-py-version:
	@echo -n "[INFO] Make is using the Python version:"
	$(PYTHON) -V

venv-app:
	@echo "####################################################"
	@echo "# CREATING THE VIRTUAL ENVIRONMENT                 #"
	@echo "####################################################"
	@echo "===================================================="
	@$(MAKE) -s log-py-version
	@echo "===================================================="
	@test -d $(VENV_DIR) || $(PYTHON) -m venv $(VENV_DIR)
	@test -d $(VENV_DIR_LINK) || ln -s $(VENV_DIR) $(VENV_DIR_LINK)
	@echo "Done!"

venv-test:
	@echo "####################################################"
	@echo "# CREATING THE >>> TEST <<< VIRTUAL ENVIRONMENT    #"
	@echo "####################################################"
	@echo "===================================================="
	@$(MAKE) -s log-py-version
	@echo "===================================================="
	@test -d $(TEST_VENV_DIR) || $(PYTHON) -m venv $(TEST_VENV_DIR);
	@echo "Done!"

#################################
#                               #
# INSTALLATION                  #
#                               #
#################################
install: venv-app
	@echo "####################################################"
	@echo "# ACTIVATING VENV & RESOLVING DEPENDENCIES         #"
	@echo "####################################################"
	@. $(VENV_DIR)/bin/activate \
		&& pip install -U pip \
		&& pip install -U .
	@echo "Done!"


install-dev: venv-test
	@echo "####################################################"
	@echo "# CREATING TESTING VENV AND INSTALLING DEV DEPS    #"
	@echo "####################################################"
	@. $(TEST_VENV_DIR)/bin/activate \
		&& pip install -U pip \
		&& pip install -e . \
		&& pip install -U -r requirements_dev.txt
	@echo "Done!"


#################################
#                               #
# TESTS AND COVERAGE            #
#                               #
#################################
VERBOSITY=vq
PYTEST_ARGS=
pytest:
	@echo "####################################################"
	@echo "# [TESTING] RUNNING ALL TESTS                      #"
	@echo "####################################################"
	@test -d $(TEST_VENV_DIR) || { echo "Please run 'make install-dev' first!"; exit 1; }
	@. $(TEST_VENV_DIR)/bin/activate && py.test --cache-clear -$(VERBOSITY) $(PYTEST_ARGS)