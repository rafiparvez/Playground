# Getting started with Airflow

## Set up
Install anacoda and follow these steps.
* Before running the set up, run these variables to ensure an error-free 
installation.
```bash
export AIRFLOW_GPL_UNIDECODE=yes
```
OR 
```bash
export SLUGIFY_USES_TEXT_UNIDECODE=yes
```
* Create a conda environment to install airflow.

```bash
conda env create -f environment.yaml
```
* Activate the conda environment
```bash
conda activate airflowenv
```

* Set up Airflow home
```bash
export AIRFLOW_HOME=~/airflow
```

## Check Airflow Version
```bash
airflow version
```

## Initialize the Airflow DB
```bash
airflow initdb
```

## Start the web server
```bash
airflow webserver
```

## 

