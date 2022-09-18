FROM apache/superset
# Switching to root to install the required packages
USER root
# Installing Trino database driver
# https://superset.apache.org/docs/databases/installing-database-drivers/
RUN pip install sqlalchemy-trino
# Switching back to using the `superset` user
USER superset